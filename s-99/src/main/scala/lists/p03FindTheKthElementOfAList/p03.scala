package lists.p03FindTheKthElementOfAList

import scala.annotation.tailrec
//scala> nth(2, List(1, 1, 2, 3, 5, 8))
//res0: Int = 2

@main
private def main() = {
  println(nth(2, List(1, 1, 2, 3, 5, 8)))
  println(nth(4, List(1, 1, 2, 3, 5, 8)))
  println(nthRecursive(2, List(1, 1, 2, 3, 5, 8)))
  println(nthRecursive(5, List(1, 1, 2, 3, 5, 8)))
}


def nth[A](n: Int, ls: List[A]): A = {
  if (n >= 0) ls(n)
  else throw new NoSuchElementException
}

@tailrec
private def nthRecursive[A](n: Int, ls: List[A]): A = (n, ls) match {
  case (0, h :: _   ) => h
  case (n, _ :: tail) => nthRecursive(n - 1, tail)
  case (_, Nil      ) => throw new NoSuchElementException
}

