package lists.p20RemoveTheKThElementFromAList

//scala> removeAt(1, List('a, 'b, 'c, 'd))
//res0: (List[Symbol], Symbol) = (List('a, 'c, 'd),'b)

@main
def main(): Unit = {
  println("input: 1, List('a', 'b', 'c', 'd')")
  println(s"output: ${removeAt(1, List('a', 'b', 'c', 'd'))}")
}


def removeAt[A](n: Int, ls: List[A]): (List[A], A) = {
  val (front, back) = ls.splitAt(n)
  (front ::: back.tail, back.head)
}