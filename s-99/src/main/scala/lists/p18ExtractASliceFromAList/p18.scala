package lists.p18ExtractASliceFromAList

//scala> slice(3, 7, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
//res0: List[Symbol] = List('d, 'e, 'f, 'g)

@main
def main(): Unit = {
  println(s"input: 3, 7, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k')")
  println(s"output: ${slice(3, 7, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'))}")

}

def slice[A](start: Int, end: Int, list: List[A]): List[A] = {
  list.slice(start, end)
}