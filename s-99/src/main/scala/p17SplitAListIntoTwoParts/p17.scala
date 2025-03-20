package p17SplitAListIntoTwoParts

@main
def main(): Unit = {
  val result = split(3, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'))
  println(result) // Output: (List('a, 'b, 'c),List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
}

def split[A](n: Int, list: List[A]): (List[A], List[A]) =
  list.splitAt(n)