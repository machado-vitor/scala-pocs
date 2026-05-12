package graphs

// Graphs P80.
// GraphBase holds the shared structure; Graph (undirected) and Digraph (directed)
// only differ in how edgeTarget resolves "the other end" of an edge, and in how
// adjacency is recorded when an edge is added.
abstract class GraphBase[T, U] {
  case class Edge(n1: Node, n2: Node, value: U) {
    def toTuple: (T, T, U) = (n1.value, n2.value, value)
  }

  case class Node(value: T) {
    var adj: List[Edge] = Nil
    def neighbors: List[Node] = adj.map(edgeTarget(_, this).get)
    override def toString: String = value.toString
  }

  var nodes: Map[T, Node] = Map()
  var edges: List[Edge] = Nil

  // For undirected graphs the edge is symmetric; for digraphs only n1 -> n2.
  def edgeTarget(e: Edge, n: Node): Option[Node]

  def addNode(value: T): Node = {
    val n = Node(value)
    nodes = nodes + (value -> n)
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
}

// Undirected graph: an edge connects both ends symmetrically.
class Graph[T, U] extends GraphBase[T, U] {
  override def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else if (e.n2 == n) Some(e.n1)
    else None

  def addEdge(n1: T, n2: T, value: U): Unit = {
    val e = Edge(nodes(n1), nodes(n2), value)
    edges = edges :+ e
    nodes(n1).adj = nodes(n1).adj :+ e
    if (n1 != n2) nodes(n2).adj = nodes(n2).adj :+ e
  }

  override def toString: String = Graph.renderString(this, '-')
}

class Digraph[T, U] extends GraphBase[T, U] {
  override def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2) else None

  def addArc(source: T, dest: T, value: U): Unit = {
    val e = Edge(nodes(source), nodes(dest), value)
    edges = edges :+ e
    nodes(source).adj = nodes(source).adj :+ e
  }

  override def toString: String = Graph.renderString(this, '>')
}

object Graph {
  // Build helpers. Unlabeled variants use Unit; labeled keep U.
  def term[T](nodes: List[T], edges: List[(T, T)]): Graph[T, Unit] =
    termLabel(nodes, edges.map { case (a, b) => (a, b, ()) })

  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]): Graph[T, U] = {
    val g = new Graph[T, U]
    nodes.foreach(g.addNode)
    edges.foreach { case (a, b, v) =>
      if (!g.nodes.contains(a)) g.addNode(a)
      if (!g.nodes.contains(b)) g.addNode(b)
      g.addEdge(a, b, v)
    }
    g
  }

  // P80: parse the human-friendly notation, unlabeled.
  def fromString(s: String): Graph[String, Unit] = {
    val (ns, es) = parse(s, '-')
    termLabel(ns, es.map { case (a, b, _) => (a, b, ()) })
  }

  // P80: parse the human-friendly notation with integer labels (e.g. "a-b/3").
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
          case Array(r) => (r, "1")  // default label when none given
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

object Digraph {
  def term[T](nodes: List[T], arcs: List[(T, T)]): Digraph[T, Unit] =
    termLabel(nodes, arcs.map { case (a, b) => (a, b, ()) })

  def termLabel[T, U](nodes: List[T], arcs: List[(T, T, U)]): Digraph[T, U] = {
    val g = new Digraph[T, U]
    nodes.foreach(g.addNode)
    arcs.foreach { case (a, b, v) =>
      if (!g.nodes.contains(a)) g.addNode(a)
      if (!g.nodes.contains(b)) g.addNode(b)
      g.addArc(a, b, v)
    }
    g
  }

  def fromString(s: String): Digraph[String, Unit] = {
    val (ns, es) = Graph.parse(s, '>')
    termLabel(ns, es.map { case (a, b, _) => (a, b, ()) })
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
  // (List(f, b, g, k, d, c, h),List((b,c,()), (f,c,()), (g,h,()), (f,b,()), (k,f,()), (h,g,())))
  // Node order follows the underlying Map iteration; edges keep insertion order.
  println(g)
  // [b-c, f-c, g-h, f-b, k-f, h-g, d]
  println(Digraph.fromStringLabel("[p>q/9, m>q/7, k, p>m/5]").toAdjacentForm)
  // List((p,List((q,9), (m,5))), (q,List()), (m,List((q,7))), (k,List()))
}
