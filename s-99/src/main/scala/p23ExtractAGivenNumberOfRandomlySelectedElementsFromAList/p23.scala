package p23ExtractAGivenNumberOfRandomlySelectedElementsFromAList

import p20RemoveTheKThElementFromAList.removeAt

import scala.util.Random

//scala> randomSelect(3, List('a, 'b, 'c, 'd, 'f, 'g, 'h))
//res0: List[Symbol] = List('e, 'd, 'a)

@main
def main(): Unit = {
  println("input: 3, List('a', 'b', 'c', 'd', 'f', 'g', 'h')")
  println(s"output: ${randomSelect(3, List('a', 'b', 'c', 'd', 'f', 'g', 'h'))}")
}

def randomSelect[A](n: Int, list: List[A]): List[A] = {
  def randomSelectR(n: Int, list: List[A], r: Random): List[A] =
    if (n <= 0) Nil
    else {
      val (rest, elem) = removeAt(r.nextInt(list.length), list) // Remove a random element from the list
      elem :: randomSelectR(n - 1, rest, r) // Add it to the result and carry on
    }

  randomSelectR(n, list, new Random)
}