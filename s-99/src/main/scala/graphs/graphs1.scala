package graphs

abstract class GraphBase[T, U] {
  case class Edge(n1: Node, n2: Node, value: U) {
    def toTuple: (T, T, U) = (n1.value, n2.value, value)
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
    val n = Node(value)
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

  // P82: cycles starting (and ending) at `source`. Drop length-3 results so
  // an undirected edge isn't reused as source→n→source.
  def findCycles(source: T): List[List[T]] =
    nodes(source).neighbors
      .flatMap(n => findPaths(n.value, source))
      .map(source :: _)
      .filter(_.lengthCompare(3) > 0)

  // P85: brute-force isomorphism — try every bijection and check that the
  // neighbor set of each node maps to the neighbor set of its image.
  def isIsomorphicTo[V, W](g: GraphBase[V, W]): Boolean =
    nodes.size == g.nodes.size && edges.size == g.edges.size && {
      val ns1 = nodes.values.toList
      g.nodes.values.toList.permutations.exists { perm =>
        val f = ns1.zip(perm).toMap
        ns1.forall(n => n.neighbors.map(f).toSet == f(n).neighbors.toSet)
      }
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
    val e = Edge(nodes(n1), nodes(n2), value)
    edges = e :: edges
    nodes(n1).adj = e :: nodes(n1).adj
    nodes(n2).adj = e :: nodes(n2).adj
  }

  // P83: every (n-1)-edge subset that keeps the graph connected is a spanning
  // tree — connected + (n-1) edges forces acyclic.
  def spanningTrees: List[Graph[T, U]] = {
    val nodeList = nodes.keys.toList
    val n = nodeList.size
    if (n == 0) List(new Graph[T, U])
    else
      edges.map(_.toTuple).combinations(n - 1).toList.flatMap { es =>
        val candidate = Graph.termLabel(nodeList, es)
        if (candidate.isConnected) Some(candidate) else None
      }
  }

  // P83: a graph is a tree iff it has exactly one spanning tree (itself).
  def isTree: Boolean = spanningTrees.lengthCompare(1) == 0

  // P84: Prim's algorithm — repeatedly pull the cheapest edge crossing from the
  // tree to a node not yet in it.
  def minimalSpanningTree(using ord: Ordering[U]): Graph[T, U] = {
    val nodeList = nodes.keys.toList
    if (nodes.isEmpty) new Graph[T, U]
    else {
      @scala.annotation.tailrec
      def grow(in: Set[Node], picked: List[Edge]): List[Edge] = {
        val crossing = edges.filter(e => in.contains(e.n1) != in.contains(e.n2))
        if (crossing.isEmpty) picked
        else {
          val cheap = crossing.minBy(_.value)
          val next = if (in.contains(cheap.n1)) cheap.n2 else cheap.n1
          grow(in + next, cheap :: picked)
        }
      }
      Graph.termLabel(nodeList, grow(Set(nodes.values.head), Nil).map(_.toTuple))
    }
  }

  // P83: BFS from any node and check every node was reached.
  def isConnected: Boolean =
    if (nodes.isEmpty) true
    else {
      @scala.annotation.tailrec
      def grow(visited: Set[Node], frontier: List[Node]): Set[Node] = frontier match {
        case Nil => visited
        case n :: rest =>
          val fresh = n.neighbors.filterNot(visited.contains)
          grow(visited ++ fresh, rest ++ fresh)
      }
      val start = nodes.values.head
      grow(Set(start), List(start)).size == nodes.size
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
    val e = Edge(nodes(source), nodes(dest), value)
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
    edges.foreach(v => g.addEdge(v._1, v._2, v._3))
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

  // P82: cycles through a given node.
  println(Graph.fromString("[b-c, f-c, g-h, d, f-b, k-f, h-g]").findCycles("f"))
  // List(List(f, c, b, f), List(f, b, c, f))

  // P83: spanning trees, isTree, isConnected.
  println(Graph.fromString("[a-b, b-c, a-c]").spanningTrees)
  // 3 spanning trees of the triangle
  val bigG = Graph.term(
    List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'),
    List(('a', 'b'), ('a', 'd'), ('b', 'c'), ('b', 'e'),
         ('c', 'e'), ('d', 'e'), ('d', 'f'), ('d', 'g'),
         ('e', 'h'), ('f', 'g'), ('g', 'h')))
  println(bigG.spanningTrees.length)
  // 112
  println(Graph.fromString("[a-b, b-c]").isTree)              // true
  println(Graph.fromString("[a-b, b-c, a-c]").isTree)         // false
  println(Graph.fromString("[a-b, b-c, a-c]").isConnected)    // true
  println(Graph.fromString("[a-b, b-c, d-e]").isConnected)    // false

  // P84: minimum spanning tree (Prim's).
  println(Graph.fromStringLabel("[a-b/1, b-c/2, a-c/3]").minimalSpanningTree)
  // [b-c/2, a-b/1] (total weight 3)
  val labeled = Graph.termLabel(
    List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'),
    List(('a', 'b', 5), ('a', 'd', 3), ('b', 'c', 2), ('b', 'e', 4),
         ('c', 'e', 6), ('d', 'e', 7), ('d', 'f', 4), ('d', 'g', 3),
         ('e', 'h', 5), ('f', 'g', 4), ('g', 'h', 1)))
  println(labeled.minimalSpanningTree)
  // an MST with total weight 22

  // P85: isomorphism.
  println(Graph.fromString("[a-b]").isIsomorphicTo(Graph.fromString("[5-7]")))
  // true
  println(Graph.fromString("[a-b, c-d]").isIsomorphicTo(Graph.fromString("[1-2, 3-4]")))
  // true — two disconnected edges either way
  println(Graph.fromString("[a-b, b-c, c-d]").isIsomorphicTo(Graph.fromString("[a-b, a-c, a-d]")))
  // false — path vs star, same |N| and |E| but different degree sequences
}
