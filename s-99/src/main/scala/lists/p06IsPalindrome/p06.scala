package lists.p06IsPalindrome

import scala.annotation.tailrec

//     scala> isPalindrome(List(1, 2, 3, 2, 1))
//     res0: Boolean = true
@main
def main(): Unit = {
  println(s"isPalindrome: ${isPalindrome(List(1,2,3,2,1))}")
}

def isPalindrome[A](ls: List[A]): Boolean = ls == ls.reverse