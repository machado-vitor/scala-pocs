package multiwaytree {

  import scala.language.implicitConversions

  case class MTree[+T](value: T, children: List[MTree[T]]) {
    def this(value: T) = this(value, List())
    override def toString: String = value.toString + children.map(_.toString).mkString + "^"

    // P70C: count the nodes of a multiway tree.
    def nodeCount: Int = 1 + children.map(_.nodeCount).sum
  }

  object MTree {
    def apply[T](value: T) = new MTree(value, List())
    def apply[T](value: T, children: List[MTree[T]]) = new MTree(value, children)

    // P70: parse a node string back into a multiway tree.
    implicit def string2MTree(s: String): MTree[Char] = {
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

  object MTree1 extends App {
    val tree = MTree('a', List(MTree('f', List(MTree('g'))), MTree('c'), MTree('b', List(MTree('d'), MTree('e')))))

    // P70
    println(tree.toString)
    // afg^^c^bd^e^^^
    println(MTree.string2MTree("afg^^c^bd^e^^^").toString)
    // afg^^c^bd^e^^^

    // P70C
    println(MTree('a', List(MTree('f'))).nodeCount)
    // 2
    println(tree.nodeCount)
    // 7
  }
}
