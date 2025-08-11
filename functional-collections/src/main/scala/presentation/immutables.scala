package presentation

@main
// A. Structural sharing
def structuralSharing(): Unit =
  val a = List(1,2,3)
  val b = 0 :: a // new head, tail points to a
  // b: List(0, 1, 2, 3)
  println(a eq b.tail)  // true  (they share the tail) // Key: Memory efficiency through shared references, not copying

@main
// B. List vs Vector basics (and the :+ trap)
def listVsVector(): Unit = // Key: Choose data structure based on access patterns
  val L = List(1,2,3)
  val L2 = 0 :: L          // O(1) prepend
  // val L3 = L :+ 4       // O(n) append — avoid in loops // Performance trap: O(n²) when appending in loops

  val V = Vector(1,2,3) // Key: Vector provides balanced O(log32 n) performance for all operations
  val V2 = V :+ 4          // amortized ~O(1) append
  // V2: Vector(1, 2, 3, 4)
  val V3 = V2.updated(1,99) // near O(1) update (log32 n)
// V3: Vector(1, 99, 3, 4)

@main
// C. Immutable Map/Set updates
def immutableMapSet(): Unit = // Key: Hash Array Mapped Trie enables efficient persistent updates
  val m1 = Map("a" -> 1, "b" -> 2)
  val m2 = m1 + ("b" -> 20) - "a"   // returns new maps; m1 unchanged
  // m2: Map(b -> 20)
  val s1 = Set(1,2,3)
  val s2 = s1 + 4 - 2
// s2: Set(1, 3, 4)