import scala.collection.mutable
import scala.util.Random

@main
def main(): Unit =
  immutableCollectionsDemo()
  mutableCollectionsDemo()
  functionalOperationsDemo()
  performanceComparison()

def immutableCollectionsDemo(): Unit =
  println("📚 IMMUTABLE COLLECTIONS")
  println("=" * 40)

  // Lists - Linked list, efficient prepend
  val numbers = List(1, 2, 3, 4, 5)
  println(s"Original List: $numbers")
  println(s"Prepend 0: ${0 :: numbers}")
  println(s"Original unchanged: $numbers")

  // Vector - Indexed access, efficient append/prepend
  val vector = Vector(1, 2, 3, 4, 5)
  println(s"\nVector: $vector")
  println(s"Updated index 2: ${vector.updated(2, 99)}")
  println(s"Appended: ${vector :+ 6}")

  // Set - Unique elements
  val set = Set(1, 2, 3, 2, 1)
  println(s"\nSet (duplicates removed): $set")
  println(s"Added element: ${set + 4}")

  // Map - Key-value pairs
  val map = Map("a" -> 1, "b" -> 2, "c" -> 3)
  println(s"\nMap: $map")
  println(s"Updated: ${map + ("d" -> 4)}")
  println(s"Get value: ${map.get("b")}")

  // Range - Lazy sequence
  val range = 1 to 10
  println(s"\nRange: $range")
  println(s"Even numbers: ${range.filter(_ % 2 == 0)}")

  println()

def mutableCollectionsDemo(): Unit =
  println("🔧 MUTABLE COLLECTIONS")
  println("=" * 40)

  // Mutable List (ListBuffer)
  val mutableList = mutable.ListBuffer(1, 2, 3)
  println(s"Mutable List: $mutableList")
  mutableList += 4
  mutableList ++= List(5, 6)
  println(s"After additions: $mutableList")

  // Mutable Array
  val array = mutable.ArrayBuffer(1, 2, 3, 4, 5)
  println(s"\nMutable Array: $array")
  array(2) = 99
  array += 6
  println(s"After modifications: $array")

  // Mutable Set
  val mutableSet = mutable.Set(1, 2, 3)
  println(s"\nMutable Set: $mutableSet")
  mutableSet += 4
  mutableSet -= 2
  println(s"After add/remove: $mutableSet")

  // Mutable Map
  val mutableMap = mutable.Map("a" -> 1, "b" -> 2)
  println(s"\nMutable Map: $mutableMap")
  mutableMap("c") = 3
  mutableMap += ("d" -> 4)
  println(s"After additions: $mutableMap")

  println()

def functionalOperationsDemo(): Unit =
  println("⚡ FUNCTIONAL OPERATIONS")
  println("=" * 40)

  val data = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  println(s"Original data: $data")

  // Map - Transform each element
  val doubled = data.map(_ * 2)
  println(s"Doubled: $doubled")

  // Filter - Select elements based on predicate
  val evens = data.filter(_ % 2 == 0)
  println(s"Even numbers: $evens")

  // FlatMap - Map and flatten
  val pairs = data.flatMap(x => List(x, x))
  println(s"Duplicated elements: $pairs")

  // Fold/Reduce - Aggregate operations
  val sum = data.reduce(_ + _)
  val product = data.foldLeft(1)(_ * _)
  println(s"Sum: $sum, Product: $product")

  // GroupBy - Group elements by a key
  val grouped = data.groupBy(_ % 3)
  println(s"Grouped by mod 3: $grouped")

  // Partition - Split into two collections
  val (odds, evenPartition) = data.partition(_ % 2 == 1)
  println(s"Partitioned - Odds: $odds, Evens: $evenPartition")

  // Zip - Combine two collections
  val letters = List('a', 'b', 'c', 'd', 'e')
  val zipped = data.take(5).zip(letters)
  println(s"Zipped: $zipped")

  // For comprehension - Syntactic sugar for map/flatMap/filter
  val result = for {
    x <- data
    if x % 2 == 0
    y <- List(10, 20)
  } yield x * y
  println(s"For comprehension result: $result")

  // Lazy evaluation with View
  val lazyResult = data.view
    .map(x => { println(s"Processing $x"); x * 2 })
    .filter(_ > 10)
    .take(3)
    .toList
  println(s"Lazy evaluation result: $lazyResult")

  println()

def performanceComparison(): Unit =
  println("⏱️ PERFORMANCE COMPARISON")
  println("=" * 40)

  val size = 100000

  // Immutable List vs Mutable ListBuffer - Append operations
  val startTime1 = System.nanoTime()
  var immutableList = List.empty[Int]
  for (i <- 1 to size) {
    immutableList = immutableList :+ i  // O(n) operation
  }
  val immutableTime = (System.nanoTime() - startTime1) / 1000000

  val startTime2 = System.nanoTime()
  val mutableBuffer = mutable.ListBuffer.empty[Int]
  for (i <- 1 to size) {
    mutableBuffer += i  // O(1) operation
  }
  val mutableTime = (System.nanoTime() - startTime2) / 1000000

  println(s"Appending $size elements:")
  println(s"Immutable List: ${immutableTime}ms")
  println(s"Mutable Buffer: ${mutableTime}ms")
  println(s"Mutable is ${immutableTime.toDouble / mutableTime}x faster for appends")

  // Vector vs List - Random access
  val vector = Vector.range(1, 10000)
  val list = List.range(1, 10000)
  val random = Random()

  val vectorStartTime = System.nanoTime()
  for (_ <- 1 to 1000) {
    val index = random.nextInt(vector.size)
    vector(index)
  }
  val vectorAccessTime = (System.nanoTime() - vectorStartTime) / 1000000

  val listStartTime = System.nanoTime()
  for (_ <- 1 to 1000) {
    val index = random.nextInt(list.size)
    list(index)
  }
  val listAccessTime = (System.nanoTime() - listStartTime) / 1000000

  println(s"\nRandom access (1000 operations):")
  println(s"Vector: ${vectorAccessTime}ms")
  println(s"List: ${listAccessTime}ms")
  println(s"Vector is ${listAccessTime.toDouble / vectorAccessTime}x faster for random access")

  // Demonstrating immutability benefits
  println(s"\n🔒 IMMUTABILITY BENEFITS")
  println("=" * 40)

  val originalList = List(1, 2, 3, 4, 5)
  val sharedReference = originalList
  val modifiedList = originalList.map(_ * 2)

  println(s"Original: $originalList")
  println(s"Shared reference: $sharedReference")
  println(s"Modified (new instance): $modifiedList")
  println(s"Original unchanged: ${originalList eq sharedReference}")
  println(s"Different instances: ${!(originalList eq modifiedList)}")

  println("\n✅ POC Complete!")
