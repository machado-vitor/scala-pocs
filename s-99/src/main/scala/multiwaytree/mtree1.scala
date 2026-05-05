package multiwaytree

case class MTree[+T](value: T, children: List[MTree[T]] = List()) {
  override def toString: String = value.toString + children.map(_.toString).mkString + "^"

  // P70C: count the nodes of a multiway tree.
  def nodeCount: Int = 1 + children.map(_.nodeCount).sum

  // P71: sum of path lengths from root to all nodes.
  def internalPathLength: Int = {
    def ipl(tree: MTree[T], depth: Int): Int =
      depth + tree.children.map(ipl(_, depth + 1)).sum
    ipl(this, 0) // this = the enclosing MTree instance.
    // when the compiler sees this, it walks through the syntactic nesting until it find a class/object/trait.
    // The first one it hits is what `this` means. Local defs, vals, blocks and lambdas don't count.
  }

  // P72: postorder sequence of the nodes
  // children left-to-right, then this node.
  def postorder: List[T] = children.flatMap(_.postorder) :+ value

  def lispyTree: String =
    if (children.isEmpty) value.toString
    else children.map(_.lispyTree).mkString(s"($value ", " ", ")")
}

object MTree {
  def apply[T](value: T) = new MTree(value, List())
  def apply[T](value: T, children: List[MTree[T]]) = new MTree(value, children)

  // P70: parse a node string back into a multiway tree.
  def fromString(s: String): MTree[Char] = {
    def parse(pos: Int): (MTree[Char], Int) = {
      val value = s(pos)
      var i = pos + 1
      val children = List.newBuilder[MTree[Char]] // O(1) appends; .result() yields the immutable List
      while (s(i) != '^') {
        val (child, next) = parse(i)
        children += child
        i = next
      }
      (MTree(value, children.result()), i + 1) // skip the '^'
    }
    parse(0)._1
  }
}

extension (s: String)
  def toMTree: MTree[Char] = MTree.fromString(s)

object MTree1 extends App {
  val tree = MTree('a', List(MTree('f', List(MTree('g'))), MTree('c'), MTree('b', List(MTree('d'), MTree('e')))))

  // P70
  println(tree.toString)
  // afg^^c^bd^e^^^
  println("afg^^c^bd^e^^^".toMTree.toString)
  // afg^^c^bd^e^^^

  // P70C
  println(MTree('a', List(MTree('f'))).nodeCount)
  // 2
  println(tree.nodeCount)
  // 7

  // P71
  println("afg^^c^bd^e^^^".toMTree.internalPathLength)
  // 9

  // P72
  println("afg^^c^bd^e^^^".toMTree.postorder)
  // List(g, f, c, d, e, b, a)
}
