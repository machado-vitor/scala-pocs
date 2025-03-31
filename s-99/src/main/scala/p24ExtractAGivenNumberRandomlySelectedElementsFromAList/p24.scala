package p24ExtractAGivenNumberRandomlySelectedElementsFromAList

import p23ExtractAGivenNumberOfRandomlySelectedElementsFromAList.randomSelect

//scala> lotto(6, 49)
//res0: List[Int] = List(23, 1, 17, 33, 21, 37)


@main
def main(): Unit = {
  println(s"input: 6, 49")
  println(s"output: ${lotto(6, 49)}")
}


def lotto(count: Int, max: Int): List[Int] =
  randomSelect(count, List.range(1, max + 1))