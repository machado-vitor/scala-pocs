package p12DecodeARunLengthEncodedList

//     scala> decode(List((4, 'a), (1, 'b), (2, 'c), (2, 'a), (1, 'd), (4, 'e)))
//     res0: List[Symbol] = List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)

@main
def main(): Unit = {
  println(decode(List((4, 'a'), (1, 'b'), (2, 'c'), (2, 'a'), (1, 'd'), (4, 'e'))))
}

def decode[A](ls: List[(Int, A)]): List[A] =
  ls.flatMap { case (count, elem) => List.fill(count)(elem) }