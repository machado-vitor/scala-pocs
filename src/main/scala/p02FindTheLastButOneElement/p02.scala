@main private def P02() = {
  penultimateBuiltin(List(1, 1, 2, 3, 5, 8))
  penultimateRecursive(List(1, 1, 2, 3, 5, 8))
  penultimateBuiltin(List(1, 2, 26, 3, 45, 7))
  penultimateRecursive(List(1, 2, 26, 3, 45, 7))
  penultimateBuiltin(List(1, 12, 42, 3, 1, 9))
  penultimateRecursive(List(1, 12, 42, 3, 1, 9))
}

private def penultimateRecursive[A](list: List[A]): A = list match {
  case h :: _ :: Nil => h      // If the list has exactly two elements, return the first one (penultimate)
  case _ :: tail     => penultimateRecursive(tail)  // Otherwise, recurse on the tail
  case _             => throw new NoSuchElementException // If the list has fewer than 2 elements, throw an error
}

private def penultimateBuiltin[A](list: List[A]): A =
  if (list.isEmpty) throw new NoSuchElementException()
  else list.init.last
  // list.init: Returns a new list without the last element.
  //list.init.last: Retrieves the last element of this new list (which is the second-to-last element of the original list).


//Comparison
//Approach	Pros	Cons
//Recursive (penultimateRecursive)	Uses pattern matching elegantly; no extra memory used	Less efficient due to recursion (O(n) calls)
//Built-in (penultimateBuiltin)	More readable and concise; efficient	Uses extra memory to create a new list with init