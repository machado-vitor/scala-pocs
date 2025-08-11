package presentation

@main
// A. Structural sharing
def structuralSharing(): Unit =
  val a = List(1,2,3)
  val b = 0 :: a // new head, tail points to a
  // b: List(0, 1, 2, 3)
  println(a eq b.tail)  // true  (they share the tail)


@main
// B. List vs Vector basics (and the :+ trap)
def listVsVector(): Unit =
  val L = List(1,2,3)
  val L2 = 0 :: L          // O(1) prepend
  // val L3 = L :+ 4       // O(n) append — avoid in loops

  val V = Vector(1,2,3)
  val V2 = V :+ 4          // amortized ~O(1) append
  // V2: Vector(1, 2, 3, 4)
  val V3 = V2.updated(1,99) // near O(1) update (log32 n)
// V3: Vector(1, 99, 3, 4)

@main
// C. Immutable Map/Set updates
def immutableMapSet(): Unit =
  val m1 = Map("a" -> 1, "b" -> 2)
  val m2 = m1 + ("b" -> 20) - "a"   // returns new maps; m1 unchanged
  // m2: Map(b -> 20)
  val s1 = Set(1,2,3)
  val s2 = s1 + 4 - 2
// s2: Set(1, 3, 4)

@main
// D. Build efficiently, publish immutable
def efficientBuilding(): Unit =
  import scala.collection.mutable.ListBuffer
  val buf = ListBuffer[Int]()
  (1 to 100000).foreach(buf += _)
  val xs: List[Int] = buf.toList   // immutable result for callers

@main
// E. Avoid intermediate allocations with view
def viewOptimization(): Unit =
  val sumFirst5Doubles =
    (1 to 1000000).view.map(_*2).filter(_%3==0).take(5).sum
