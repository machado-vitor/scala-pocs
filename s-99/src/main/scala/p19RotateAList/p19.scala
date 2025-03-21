package p19RotateAList

@main def main(): Unit = {
  val list = List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k')
  println(s"rotate(3, $list): ${rotate(3, list)}")
  println(s"rotate(-2, $list): ${rotate(-2, list)}")
}

def rotate[A](n: Int, list: List[A]): List[A] =
  if (list.isEmpty) list
  else {
    val nNorm = ((n % list.length) + list.length) % list.length // Normalize n, so it's in the range 0 until list.length
    list.drop(nNorm) ++ list.take(nNorm)
  }
