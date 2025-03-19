package p16DropEveryNthElementFromAList

//input: List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k')
//output: List(a, b, d, e, g, h, j, k)

@main
def main(): Unit = {
  println(s"input: List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k')")
  println(s"output: ${drop(3, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'))}")
}

def drop[A](n: Int, ls: List[A]): List[A] =
  ls.zipWithIndex.collect { case (element, index) if (index + 1) % n != 0 => element }
  //The .zipWithIndex method pairs each element in the list with its index.
  //collect is a combination of map and filter.
  //It allows pattern matching inside { case ... }.