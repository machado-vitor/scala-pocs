

@main
// Transform: map, flatMap, collect
def transform(): Unit =
  val words = List("hi", "scala", "io")
  // map: 1-to-1
  println(words.map(_.length)) // List(2,5,2)
  // flatMap: 1-to-many
  println(words.flatMap(_.toCharArray).distinct) // List(h, i, s, c, a, l, o)
  // collect: filter + map in one pass
  val nums: List[Any] = List(1, "x", 2, 3.0, 4)
  println(nums.collect { case i: Int if i % 2 == 0 => i * 10 }) // List(20, 40)


@main
// Select: filter, partition, find
def select(): Unit =
  val xs = Vector.range(1, 10)
  println(xs.filter(_ % 2 == 0)) // Vector(2,4,6,8)
  println(xs.partition(_ < 5)) // (Vector(1,2,3,4),Vector(5,6,7,8,9))
  println(xs.find(_ > 7)) // Some(8)

@main
def combine(): Unit =
  val ys = List(1, 2, 3, 4)
  println(ys.foldLeft(0)(_ + _)) // 10
  println(ys.scanLeft(0)(_ + _)) // List(0,1,3,6,10)  // running sums

