package p21InsertAnElementAtAGivenPositionIntoAList

//scala> insertAt('new, 1, List('a, 'b, 'c, 'd))
//res0: List[Symbol] = List('a, 'new, 'b, 'c, 'd)


@main
def main(): Unit = {
  println("input: \"new\", 1, List('a', 'b', 'c', 'd')")
  println(s"output: ${insertAt("new", 1, List('a', 'b', 'c', 'd'))}")
}


def insertAt[A](elem: A, n: Int, ls: List[A]): List[A] = {
  val (front, back) = ls.splitAt(n)
  front ::: elem :: back
}