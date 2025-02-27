package p05ReverseAList

import scala.annotation.tailrec

//     scala> reverse(List(1, 1, 2, 3, 5, 8))
//     res0: List[Int] = List(8, 5, 3, 2, 1, 1)
@main
def main(): Unit = {
  val list = List.fill(100000)(1)

  println(s"Built-in reverse: ${timeExecution(reverse(list)).length}")
  println(s"Tail-recursive reverse: ${timeExecution(reverseTailRecursiva(list)).length}")
  println(s"Functional reverse: ${timeExecution(reverseFunctional(list)).length}")
}

def reverse[A](ls: List[A]): List[A] = ls.reverse

def reverseTailRecursiva[A](ls: List[A]): List[A] = {
  @tailrec
  def reverseR(result: List[A], curList: List[A]): List[A] = curList match {
    case Nil => result
    case h :: tail => reverseR(h :: result, tail)
  }
  reverseR(Nil, ls)
}

def reverseFunctional[A](ls: List[A]): List[A] =
  ls.foldLeft(List[A]()) { (r,h) => h :: r }

private def timeExecution[A](block: => A): A = {
  val start = System.nanoTime()
  val result = block
  val end = System.nanoTime()
  println(s"Time taken: ${(end - start) / 1e6} ms")
  result
}