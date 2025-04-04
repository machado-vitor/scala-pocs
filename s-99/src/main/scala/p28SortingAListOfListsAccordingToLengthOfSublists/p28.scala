package p28SortingAListOfListsAccordingToLengthOfSublists
//scala> lsort(List(List('a, 'b, 'c), List('d, 'e), List('f, 'g, 'h), List('d, 'e), List('i, 'j, 'k, 'l), List('m, 'n), List('o)))
//res0: List[List[Symbol]] = List(List('o), List('d, 'e), List('d, 'e), List('m, 'n), List('a, 'b, 'c), List('f, 'g, 'h), List('i, 'j, 'k, 'l))

//scala> lsortFreq(List(List('a, 'b, 'c), List('d, 'e), List('f, 'g, 'h), List('d, 'e), List('i, 'j, 'k, 'l), List('m, 'n), List('o)))
//res1: List[List[Symbol]] = List(List('i, 'j, 'k, 'l), List('o), List('a, 'b, 'c), List('f, 'g, 'h), List('d, 'e), List('d, 'e), List('m, 'n))

@main
def main(): Unit = {
  val input = List(
    List('a', 'b', 'c'),
    List('d', 'e'),
    List('f', 'g', 'h'),
    List('d', 'e'),
    List('i', 'j', 'k', 'l'),
    List('m', 'n'),
    List('o')
  )

  println("input: " + input)
  println(s"lsort output: ${lsort(input)}")
  println(s"lsortFreq output: ${lsortFreq(input)}")
}

def lsort[A](list: List[List[A]]): List[List[A]] = {
  list.sortBy(_.length)
}

def lsortFreq[A](list: List[List[A]]): List[List[A]] = {
  val lengthFreq: Map[Int, Int] = list.groupBy(_.length).view.mapValues(_.size).toMap
  list.sortBy(sublist => lengthFreq(sublist.length))
}

