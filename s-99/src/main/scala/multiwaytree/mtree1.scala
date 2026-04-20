package multiwaytree {

  case class MTree[+T](value: T, children: List[MTree[T]]) {
    def this(value: T) = this(value, List())
    override def toString: String = "M(" + value.toString + " {" + children.map(_.toString).mkString(",") + "})"
  }

  object MTree {
    def apply[T](value: T) = new MTree(value, List())
    def apply[T](value: T, children: List[MTree[T]]) = new MTree(value, children)
  }

  object MTree1 extends App {
    val tree = MTree('a', List(MTree('f', List(MTree('g'))), MTree('c'), MTree('b', List(MTree('d'), MTree('e')))))
    println(tree)
    // M(a {M(f {M(g {})}),M(c {}),M(b {M(d {}),M(e {})})})
  }
}
