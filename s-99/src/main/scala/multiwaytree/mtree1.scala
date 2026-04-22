package multiwaytree {

  case class MTree[+T](value: T, children: List[MTree[T]]) {
    def this(value: T) = this(value, List())
    override def toString: String = value.toString + children.map(_.toString).mkString + "^"

    // P70C: count the nodes of a multiway tree.
    def nodeCount: Int = 1 + children.map(_.nodeCount).sum

    // P71: sum of path lengths from root to all nodes.
    def internalPathLength: Int = {
      def ipl(tree: MTree[T], depth: Int): Int =
        depth + tree.children.map(ipl(_, depth + 1)).sum
      ipl(this, 0)
    }
  }

  object MTree {
    def apply[T](value: T) = new MTree(value, List())
    def apply[T](value: T, children: List[MTree[T]]) = new MTree(value, children)

    // P70: parse a node string back into a multiway tree.
    def fromString(s: String): MTree[Char] = {
      def parse(pos: Int): (MTree[Char], Int) = {
        val value = s(pos)
        var i = pos + 1
        val children = List.newBuilder[MTree[Char]]
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
  }
}
