package p13RunLengthEncodingOfAList

//     Example:
//     scala> encodeDirect(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
//     res0: List[(Int, Symbol)] = List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e))

@main
def main(): Unit = {
  val inputList = List('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e')
  val encodedList = encodeDirect(inputList)
  println(encodedList)
}

def encodeDirect[A](ls: List[A]): List[(Int, A)] =
  if (ls.isEmpty) Nil
  else {
    val (packed, next) = ls.span { _ == ls.head } // span splits the list in two parts
    (packed.length, packed.head) :: encodeDirect(next) // concatenate with the next part
  }
