package lists.p01FindTheLastElementOfAList

import scala.annotation.tailrec

@main
private def main() = {
  last(List(1, 1, 2, 3, 5, 8))
  lastRecursive(List(1, 1, 2, 3, 5, 8))
  last(List(1, 2, 26, 3, 45, 7))
  lastRecursive(List(1, 2, 26, 3, 45, 7))
  last(List(1, 12, 42, 3, 1, 9))
  lastRecursive(List(1, 12, 42, 3, 1, 9))
}

private def last(list: List[Int]): Int = {
  println(s"input $list")
  val last = list.last
  println(s"output $last")
  last
}

@tailrec
private def lastRecursive[A](ls: List[A]): A = ls match {
  case h :: Nil  => h
  case _ :: tail => lastRecursive(tail)
  case _         => throw new NoSuchElementException
}