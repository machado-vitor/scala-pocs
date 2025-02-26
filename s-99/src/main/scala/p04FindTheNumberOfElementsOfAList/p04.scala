package p04FindTheNumberOfElementsOfAList

import scala.annotation.tailrec

// scala> length(List(1, 1, 2, 3, 5, 8))
// res0: Int = 6

@main
private def main(): Unit = {
  val list = List.fill(10000)(1)

  println(s"Built-in length: ${timeExecution(length(list))}")
  println(s"Recursive length: ${timeExecution(lengthRecursive(list))}")
  println(s"Tail-recursive length: ${timeExecution(lengthTailRecursive(list))}")
  println(s"Functional length (foldLeft): ${timeExecution(lengthFunctional(list))}")
}

private def timeExecution[A](block: => A): A = {
  val start = System.nanoTime()
  val result = block
  val end = System.nanoTime()
  println(s"Time taken: ${(end - start) / 1e6} ms")
  result
}

private def length[A](ls: List[A]): Int = ls.length

private def lengthRecursive[A](ls: List[A]): Int = ls match {
  case Nil       => 0
  case _ :: tail => 1 + lengthRecursive(tail)
}

private def lengthTailRecursive[A](ls: List[A]): Int = {
  @tailrec
  def lengthR(result: Int, curList: List[A]): Int = curList match {
    case Nil       => result
    case _ :: tail => lengthR(result + 1, tail)
  }
  lengthR(0, ls)
}

private def lengthFunctional[A](ls: List[A]): Int = ls.foldLeft(0) { (c, _) => c + 1 }
