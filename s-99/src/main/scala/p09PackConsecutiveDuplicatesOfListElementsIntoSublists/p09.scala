package p09PackConsecutiveDuplicatesOfListElementsIntoSublists

// scala> pack(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
// res0: List[List[Symbol]] = List(List('a, 'a, 'a, 'a), List('b), List('c, 'c), List('a, 'a), List('d), List('e, 'e, 'e, 'e))
@main
def main(): Unit = {
  println("List('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e')")
  println(pack(List('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e')))
}


def pack[A](ls: List[A]): List[List[A]] = {
  if (ls.isEmpty) List(List())
  else {
    val (packed, next) = ls span { _ == ls.head } // span splits the list into two parts:
    //packed → The longest prefix where all elements are equal to ls.head.
    //next → The remaining list (everything after packed).
    if (next == Nil) List(packed) // end of the list, return
    else packed :: pack(next) // if not, pack the remaining list, group it with the packed list, and return
  }
}

