// Arrays and String support the same operations as Seq
@main
def arrays_and_strings(): Unit =
  val xs: Array[Int] = Array(1, 2, 3)
  xs.map(_ + 1)
  val ys: String = "abc"
  ys.filter(_.isUpper)

