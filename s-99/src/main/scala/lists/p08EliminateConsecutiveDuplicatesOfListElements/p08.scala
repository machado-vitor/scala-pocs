package lists.p08EliminateConsecutiveDuplicatesOfListElements

import scala.annotation.tailrec

//     scala> compress(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
//     res0: List[Symbol] = List('a, 'b, 'c, 'a, 'd, 'e)
@main
def main(): Unit = {
  println(compress(List('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e')))
}

def compress[A](ls: List[A]): List[A] = {
  @tailrec
  def compressR(result: List[A], curList: List[A]): List[A] = curList match {
    case h :: tail => compressR(h :: result, tail.dropWhile(_ == h)) // call recursively with the rest of the list
    // h is the first element of the list, tail.dropWhile(_ == h)
    // will remove from the list all the consecutive elements that are equal to h
    case Nil       => result.reverse
    // once the list is empty, recursion stops, and we return result.reverse because it was inserted in reverse order
  }
  compressR(Nil, ls)
}