// Ranges
// has three operations: to (inclusive), until (exclusive) and by (to determine step value)
// And three fields: lower bound, upper bound and step value.

@main
def ranges(): Unit =
  val r: Range = 1 to 10 by 2
  println(r) // inexact Range 1 to 10 by 2
  val r2: Range = 10 until 1 by -2
  println(r2) // inexact Range 10 until 1 by -2
  val r3: Range = 2 to 10 by 2
  println(r3) // Range 2 to 10 by 2
  val r4: Range = 10 until 2 by -2
  println(r4) // Range 10 until 2 by -2


