package lists.p14DuplicateTheElementsOfAList

//input: List('a', 'b', 'c', 'c', 'd')
//output: List(a, a, b, b, c, c, c, c, d, d)

@main
def main(): Unit = {
  val inputList = List('a', 'b', 'c', 'c', 'd')
  println("input: List('a', 'b', 'c', 'c', 'd')")
  println(s"output: ${duplicate(inputList)}")
}

def duplicate[A](ls: List[A]): List[A] = ls.flatMap { e => List(e, e) }