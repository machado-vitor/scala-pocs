import scala.collection.mutable
import scala.util.Random

@main
def main(): Unit =
  immutableCollectionsDemo()
  mutableCollectionsDemo()
  functionalOperationsDemo()
  performanceComparison()

def immutableCollectionsDemo(): Unit =
  // Immutable collections are thread-safe and prevent accidental mutations.
  // Operations on immutable collections return new instances rather than modifying the original.
  println("📚 IMMUTABLE COLLECTIONS")
  println("=" * 40)

  // Lists - Linked list, efficient prepend
  val numbers = List(1, 2, 3, 4, 5)
  println(s"Original List: $numbers")
  println(s"Prepend 0: ${0 :: numbers}")
  println(s"Original unchanged: $numbers")
  numbers ::: numbers // the elements of this will appers twice (same reference in memory) on the same list

  // List methods:
  // :: adds an element in front of the list
  // ::: adds the elements of a list in front of the list

  // Vector - Indexed access, efficient append/prepend
  val vector = Vector(1, 2, 3, 4, 5) // create new vector
  println(s"\nVector: $vector")
  println(s"Updated index 2: ${vector.updated(2, 99)}") // this creates a new vector at index 2 replaced by 99
  // original vector remains unchanged.
  println(s"Appended: ${vector :+ 6}")

  // Set - Unique elements
  val set = Set(1, 2, 3, 2, 1) // duplicates are automatically removed.
  // set are unordered collections.
  println(s"\nSet (duplicates removed): $set")
  println(s"Added element: ${set + 4}") // creates a new set with 4 added. Original remains unchanged.

  // Map - Key-value pairs
  val map = Map("a" -> 1, "b" -> 2, "c" -> 3)
  //Creation: creates a new immutable map with three key-value pairs
  println(s"\nMap: $map")
  println(s"Updated: ${map + ("d" -> 4)}")
  // adding a new pair returns a new map instance. Original map ramins unchanged.
  println(s"Get value: ${map.get("b")}") // returns an Option.

  // Range - Lazy sequence
  // Represents a sequence of integers without storing all values in memory.
  val range = 1 to 10
  println(s"\nRange: $range")
  // values are computed on-demand
  println(s"Even numbers: ${range.filter(_ % 2 == 0)}")

  // The filter operation is lazy - no computation yet
  val evenNumbers = (1 to 10).filter(_ % 2 == 0)
  println(s"Filter created (lazy): $evenNumbers")

  // Computation happens when you materialize the result
  val materializedEvens = evenNumbers.toList
  println(s"Materialized result: $materializedEvens")

  //Ranges are ideal for loops, iterations,
  // and mathematical sequences where you don't need all values at once.

  println()

def mutableCollectionsDemo(): Unit =
  // Mutable collections are not thread-safe by default and require explicit synchronization for concurrent access.
  println("🔧 MUTABLE COLLECTIONS")
  println("=" * 40)

  // Mutable List (ListBuffer)
  // is efficient for frequent additions and mutations.
  val mutableList = mutable.ListBuffer(1, 2, 3) // matable and growable list
  println(s"Mutable List: $mutableList")
  mutableList += 4 // appends 4 to the buffer.
  // ++= alias for addAll
  mutableList ++= List(5, 6) // appends all elements from another list
  println(s"After additions: $mutableList")

  // Mutable Array
  // should be used when you need fast, mutable, indexed storage.
  val array = Array(1, 2, 3, 4, 5)
  println(s"\nMutable Array: ${array.mkString("[", ", ", "]")}")
  array(2) = 99 // Arrays allow direct, in-place mutation and are efficient for indexed access and updates.
  println(s"After mutation: ${array.mkString("[", ", ", "]")}")

  // Mutable Set
  // All changes directly update the original set.
  val mutableSet = mutable.Set(1, 2, 3)
  println(s"\nMutable Set: $mutableSet")
  mutableSet += 4 // adds 4 to the set
  mutableSet ++= Set(5, 6) // adds multiple elements at once.
  println(s"After additions: $mutableSet")

  // Mutable Map
  //
  val mutableMap = mutable.Map("a" -> 1, "b" -> 2)
  println(s"\nMutable Map: $mutableMap")
  mutableMap("c") = 3 // sets the value for key "c" directly, adding it if missing or updating if present.
  mutableMap += ("d" -> 4) // adds the key-value pair ("d", 4) to the map, or updates "d" if it exists.
  // These two do the same, the only difference is the syntax.
  println(s"After additions: $mutableMap")

  println()

def functionalOperationsDemo(): Unit =
  println("⚡ FUNCTIONAL OPERATIONS")
  println("=" * 40)

  val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  // Taken from List javadoc:
  //This class is optimal for last-in-first-out (LIFO), stack-like access patterns.
  // If you need another access pattern, for example, random access or FIFO,
  // consider using a collection more suited to this than List.
  println(s"Original numbers: $numbers")

  // Map - Transform each element
  val doubled = numbers.map(_ * 2) // Creates a new list.
  println(s"Doubled: $doubled")

  // Filter - Select elements matching condition
  val evens = numbers.filter(_ % 2 == 0)
  println(s"Even numbers: $evens")

  // Fold/Reduce - Aggregate operations
  val sum = numbers.foldLeft(0)(_ + _)
  val product = numbers.reduce(_ * _)
  println(s"Sum: $sum, Product: $product")

  // FlatMap - Map and flatten
  val words = List("hello", "world", "scala")
  val letters = words.flatMap(_.toCharArray)
  println(s"All letters: $letters")

  // GroupBy - Group elements by a key
  val grouped = numbers.groupBy(_ % 3)
  println(s"Grouped by remainder when divided by 3: $grouped")

  // Chaining operations
  val result = numbers
    .filter(_ % 2 == 0)
    .map(_ * 3)
    .filter(_ > 10)
    .take(3)
  println(s"Chained operations result: $result")

  // For comprehensions
  val pairs = for {
    x <- List(1, 2, 3)
    y <- List("a", "b")
  } yield (x, y)
  println(s"Cartesian product: $pairs")

  // Option handling
  val maybeNumber = Some(42)
  val doubled2 = maybeNumber.map(_ * 2)
  println(s"Option mapping: $doubled2")

  println()

def performanceComparison(): Unit =
  println("⏱️  PERFORMANCE COMPARISON")
  println("=" * 40)

  val size = 100000

  // Immutable List prepend
  val start1 = System.nanoTime()
  var immutableList = List.empty[Int]
  for (i <- 1 to size) {
    immutableList = i :: immutableList
  }
  val time1 = (System.nanoTime() - start1) / 1e6
  println(s"Immutable List prepend ($size elements): ${time1}ms")

  // Mutable ListBuffer append
  val start2 = System.nanoTime()
  val mutableBuffer = mutable.ListBuffer.empty[Int]
  for (i <- 1 to size) {
    mutableBuffer += i
  }
  val time2 = (System.nanoTime() - start2) / 1e6
  println(s"Mutable ListBuffer append ($size elements): ${time2}ms")

  // Vector append
  val start3 = System.nanoTime()
  var vector = Vector.empty[Int]
  for (i <- 1 to size) {
    vector = vector :+ i
  }
  val time3 = (System.nanoTime() - start3) / 1e6
  println(s"Vector append ($size elements): ${time3}ms")

  // Array operations
  val start4 = System.nanoTime()
  val array = Array.ofDim[Int](size)
  for (i <- 0 until size) {
    array(i) = i + 1
  }
  val time4 = (System.nanoTime() - start4) / 1e6
  println(s"Array direct assignment ($size elements): ${time4}ms")

  println()
  println("🎯 KEY TAKEAWAYS")
  println("=" * 40)
  println("1. Immutable collections are thread-safe and prevent accidental mutations")
  println("2. Mutable collections offer better performance for frequent updates")
  println("3. Choose List for frequent prepends, Vector for random access")
  println("4. Functional operations enable elegant data transformations")
  println("5. For comprehensions provide readable syntax for complex operations")
  println("6. Always consider the performance characteristics of your chosen collection")
