package graphs

abstract class GraphBase[T, U] {
  case class Edge(n1: Node, n2: Node, value: U) {
    def toTuple = (n1.value, n2.value, value)
  }
  case class Node(value: T) {
    var adj: List[Edge] = Nil
    // neighbors are all nodes adjacent to this node.
    def neighbors: List[Node] = adj.map(edgeTarget(_, this).get)
  }

  var nodes: Map[T, Node] = Map()
  var edges: List[Edge] = Nil

  // If the edge E connects N to another node, returns the other node,
  // otherwise returns None.
  def edgeTarget(e: Edge, n: Node): Option[Node]

  override def equals(o: Any): Boolean = o match {
    case g: GraphBase[?, ?] =>
      nodes.keys.toList.diff(g.nodes.keys.toList).isEmpty &&
      edges.map(_.toTuple).diff(g.edges.map(_.toTuple)).isEmpty
    case _ => false
  }

  def addNode(value: T): Node = {
    val n = new Node(value)
    nodes = Map(value -> n) ++ nodes
    n
  }

  // P80: graph-term form.
  def toTermForm: (List[T], List[(T, T, U)]) =
    (nodes.keys.toList, edges.map(_.toTuple))

  // P80: adjacency-list form.
  def toAdjacentForm: List[(T, List[(T, U)])] =
    nodes.values.toList.map { n =>
      (n.value, n.adj.map(e => (edgeTarget(e, n).get.value, e.value)))
    }

  // P81: all acyclic paths from `from` to `to`. Direction (or lack of it) is
  // already encoded in `neighbors` via `edgeTarget`, so the same DFS works for
  // both Graph and Digraph.
  def findPaths(from: T, to: T): List[List[T]] = {
    def step(curr: Node, visited: Set[T]): List[List[T]] =
      if (curr.value == to) List(List(to))
      else
        curr.neighbors
          .filterNot(n => visited.contains(n.value))
          .flatMap(n => step(n, visited + n.value).map(curr.value :: _))
    step(nodes(from), Set(from))
  }
}

class Graph[T, U] extends GraphBase[T, U] {
  override def equals(o: Any): Boolean = o match {
    case g: Graph[?, ?] => super.equals(g)
    case _ => false
  }

  def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else if (e.n2 == n) Some(e.n1)
    else None

  def addEdge(n1: T, n2: T, value: U): Unit = {
    val e = new Edge(nodes(n1), nodes(n2), value)
    edges = e :: edges
    nodes(n1).adj = e :: nodes(n1).adj
    nodes(n2).adj = e :: nodes(n2).adj
  }

  override def toString: String = Graph.renderString(this, '-')
}

class Digraph[T, U] extends GraphBase[T, U] {
  override def equals(o: Any): Boolean = o match {
    case g: Digraph[?, ?] => super.equals(g)
    case _ => false
  }

  def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else None

  def addArc(source: T, dest: T, value: U): Unit = {
    val e = new Edge(nodes(source), nodes(dest), value)
    edges = e :: edges
    nodes(source).adj = e :: nodes(source).adj
  }

  override def toString: String = Graph.renderString(this, '>')
}

abstract class GraphObjBase {
  type GraphClass[T, U]

  def addLabel[T](edges: List[(T, T)]): List[(T, T, Unit)] =
    edges.map(v => (v._1, v._2, ()))
  def term[T](nodes: List[T], edges: List[(T, T)]): GraphClass[T, Unit] =
    termLabel(nodes, addLabel(edges))
  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]): GraphClass[T, U]

  def addAdjacentLabel[T](nodes: List[(T, List[T])]): List[(T, List[(T, Unit)])] =
    nodes.map(a => (a._1, a._2.map((_, ()))))
  def adjacent[T](nodes: List[(T, List[T])]): GraphClass[T, Unit] =
    adjacentLabel(addAdjacentLabel(nodes))
  def adjacentLabel[T, U](nodes: List[(T, List[(T, U)])]): GraphClass[T, U]
}

object Graph extends GraphObjBase {
  type GraphClass[T, U] = Graph[T, U]

  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]): Graph[T, U] = {
    val g = new Graph[T, U]
    nodes.map(g.addNode)
    edges.map(v => g.addEdge(v._1, v._2, v._3))
    g
  }
  def adjacentLabel[T, U](nodes: List[(T, List[(T, U)])]): Graph[T, U] = {
    val g = new Graph[T, U]
    for ((v, _) <- nodes) g.addNode(v)
    for ((n1, a) <- nodes; (n2, l) <- a)
      if (!g.nodes(n1).neighbors.contains(g.nodes(n2)))
        g.addEdge(n1, n2, l)
    g
  }

  // P80: parse the human-friendly notation.
  def fromString(s: String): Graph[String, Unit] = {
    val (ns, es) = parse(s, '-')
    term(ns, es.map { case (a, b, _) => (a, b) })
  }

  def fromStringLabel(s: String): Graph[String, Int] = {
    val (ns, es) = parse(s, '-')
    termLabel(ns, es.map { case (a, b, l) => (a, b, l.toInt) })
  }

  // Shared parser used by both Graph and Digraph; the separator picks the form.
  private[graphs] def parse(s: String, sep: Char): (List[String], List[(String, String, String)]) = {
    val body = s.trim.stripPrefix("[").stripSuffix("]")
    val tokens = if (body.isEmpty) Nil else body.split(",").toList.map(_.trim).filter(_.nonEmpty)
    val ns = scala.collection.mutable.LinkedHashSet[String]()
    val es = scala.collection.mutable.ListBuffer[(String, String, String)]()
    tokens.foreach { tok =>
      val idx = tok.indexOf(sep)
      if (idx < 0) ns += tok
      else {
        val left = tok.substring(0, idx)
        val rest = tok.substring(idx + 1)
        val (right, label) = rest.split('/') match {
          case Array(r) => (r, "1") // default label when none given
          case Array(r, l) => (r, l)
        }
        ns += left
        ns += right
        es += ((left, right, label))
      }
    }
    (ns.toList, es.toList)
  }

  // Render Graph/Digraph: edges first, then isolated nodes. The `/label` suffix
  // is dropped when U is Unit so unlabeled output stays clean.
  private[graphs] def renderString[T, U](g: GraphBase[T, U], sep: Char): String = {
    val edgeStrs = g.edges.map { e =>
      val label = e.value match {
        case () => ""
        case v => s"/$v"
      }
      s"${e.n1.value}$sep${e.n2.value}$label"
    }
    val touched = g.edges.flatMap(e => List(e.n1.value, e.n2.value)).toSet
    val isolated = g.nodes.keys.filterNot(touched.contains).map(_.toString).toList
    (edgeStrs ++ isolated).mkString("[", ", ", "]")
  }
}

object Digraph extends GraphObjBase {
  type GraphClass[T, U] = Digraph[T, U]

  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]): Digraph[T, U] = {
    val g = new Digraph[T, U]
    nodes.map(g.addNode)
    edges.map(v => g.addArc(v._1, v._2, v._3))
    g
  }
  def adjacentLabel[T, U](nodes: List[(T, List[(T, U)])]): Digraph[T, U] = {
    val g = new Digraph[T, U]
    for ((n, _) <- nodes) g.addNode(n)
    for ((s, a) <- nodes; (d, l) <- a) g.addArc(s, d, l)
    g
  }

  def fromString(s: String): Digraph[String, Unit] = {
    val (ns, es) = Graph.parse(s, '>')
    term(ns, es.map { case (a, b, _) => (a, b) })
  }

  def fromStringLabel(s: String): Digraph[String, Int] = {
    val (ns, es) = Graph.parse(s, '>')
    termLabel(ns, es.map { case (a, b, l) => (a, b, l.toInt) })
  }
}

object Graphs1 extends App {
  // P80: conversions and human-friendly form.
  val g = Graph.fromString("[b-c, f-c, g-h, d, f-b, k-f, h-g]")
  println(g.toTermForm)
  // (List(f, b, g, k, d, c, h),List((h,g,()), (k,f,()), (f,b,()), (g,h,()), (f,c,()), (b,c,())))
  // Node order is HashMap iteration order; edges are reverse-insertion (`::`).
  println(g)
  // [h-g, k-f, f-b, g-h, f-c, b-c, d]
  println(Digraph.fromStringLabel("[p>q/9, m>q/7, k, p>m/5]").toAdjacentForm)
  // List((k,List()), (m,List((q,7))), (q,List()), (p,List((m,5), (q,9))))

  // P81: acyclic paths between two nodes.
  val dg = Digraph.fromStringLabel("[p>q/9, m>q/7, k, p>m/5]")
  println(dg.findPaths("p", "q"))
  // List(List(p, q), List(p, m, q))
  println(dg.findPaths("p", "k"))
  // List()
}
