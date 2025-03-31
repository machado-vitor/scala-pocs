package p26GenerateTheCombinationOfKDistinctObjectsChosenFromTheNElementsOfAList

//scala> combinations(3, List('a, 'b, 'c, 'd, 'e, 'f))
//res0: List[List[Symbol]] = List(List('a, 'b, 'c), List('a, 'b, 'd), List('a, 'b, 'e), ...

@main
def main(): Unit = {
  println("input: 3, List('a, 'b, 'c, 'd, 'e, 'f)")
  println(s"output: ${combinations(3, List('a', 'b', 'c', 'd', 'e', 'f'))}")
}

def combinations[A](n: Int, list: List[A]): List[List[A]] = {
  if (n == 0) List(Nil) // base case: one way to choose 0 elements
  else if (n > list.length) Nil // not enough elements to choose from
  else { // recursive case
    for {
      i <- list.indices.toList
      rest <- combinations(n - 1, list.drop(i + 1)) // choose the i-th element and find combinations of n-1 from the rest
    } yield list(i) :: rest // prepend the i-th element to the combinations of n-1
  }
}