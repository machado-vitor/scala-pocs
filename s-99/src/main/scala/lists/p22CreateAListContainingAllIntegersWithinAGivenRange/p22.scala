package lists.p22CreateAListContainingAllIntegersWithinAGivenRange

//scala> range(4, 9)
//res0: List[Int] = List(4, 5, 6, 7, 8, 9)


@main
def main(): Unit = {
  println(s"input: 4, 9")
  println(s"output: ${range(4, 9)}")
}

def range(start: Int, end: Int): List[Int] = {
  List.range(start, end + 1)
}