package lists.p25GenerateARandomPermutationOfTheElementsOfAList

import lists.p23ExtractAGivenNumberOfRandomlySelectedElementsFromAList.randomSelect

//scala> randomPermute(List('a, 'b, 'c, 'd, 'e, 'f))
//res0: List[Symbol] = List('b, 'a, 'd, 'c, 'e, 'f)

@main
def main(): Unit = {
  println("input: List('a, 'b, 'c, 'd, 'e, 'f)")
  println(s"output: ${randomPermute(List('a', 'b', 'c', 'd', 'e', 'f'))}")
}

def randomPermute[A](list: List[A]): List[A] = {
  randomSelect(list.length, list)
}