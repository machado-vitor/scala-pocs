package p15DuplicateTheElementsOfAListAGivenNumberOfTimes

//input: List('a', 'b', 'c', 'c', 'd')
//output: List(a, a, a, b, b, b, c, c, c, c, c, c, d, d, d)

@main
def main(): Unit = {
  println(s"input: List('a', 'b', 'c', 'c', 'd')")
  println(s"output: ${duplicateN(3, List('a', 'b', 'c', 'c', 'd'))}")
}

def duplicateN[A](n: Int, ls: List[A]): List[A] = ls.flatMap(e => List.fill(n)(e))
