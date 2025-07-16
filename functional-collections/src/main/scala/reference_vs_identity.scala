@main
def reference_vs_identity(): Unit =
  val list = List(1, 2, 3)
  val vector = Vector(1, 2, 3)
  val set = Set(1, 2, 3)

  // For small integers (-128 to 127), JVM uses cached instances
  val a = 1
  val b = 1
  println(a eq b) // true - same object reference

  val x = 1000
  val y = 1000
  println(x eq y) // false - different object references
  println(x == y) // true - same value