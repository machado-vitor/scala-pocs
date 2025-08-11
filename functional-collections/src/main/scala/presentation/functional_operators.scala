package presentation

@main
// 1) Transform: map, flatMap, collect
def transformOps(): Unit = // Key: Different transformation patterns for different use cases
  val words = List("hi", "scala", "io")
  // map: 1-to-1
  println(words.map(_.length)) // List(2,5,2) // Standard functor operation - preserves structure
  // flatMap: 1-to-many
  println(words.flatMap(_.toCharArray).distinct) // List(h, i, s, c, a, l, o) // Monadic bind - flattens nested structures
  // collect: filter + map in one pass
  val nums: List[Any] = List(1, "x", 2, 3.0, 4)
  nums.collect { case i: Int if i % 2 == 0 => i * 10 } // List(20, 40) // Partial function optimization - single traversal


@main
// 2) Select: filter, partition, find
def selectOps(): Unit = // Key: Different selection strategies based on cardinality needs
  val xs = Vector.range(1, 10)
  println(xs.filter(_ % 2 == 0)) // Vector(2,4,6,8) // All matching elements
  println(xs.partition(_ < 5)) // (Vector(1,2,3,4),Vector(5,6,7,8,9)) // Split into matching/non-matching - single pass
  println(xs.find(_ > 7)) // Some(8) // Short-circuit evaluation - stops at first match

@main
// 3) Combine: foldLeft (safe), scan (running totals)
def combineOps(): Unit = // Key: Fold patterns for aggregation and intermediate results
  val ys = List(1, 2, 3, 4)
  println(ys.foldLeft(0)(_ + _)) // 10 // Tail-recursive accumulation - stack safe
  println(ys.scanLeft(0)(_ + _)) // List(0,1,3,6,10)  // running sums // Fold + intermediate results - useful for running calculations

@main
// 4) Grouping done right: groupMapReduce (Scala 2.13+)
def grouping(): Unit = // Key: Single-pass grouping avoids intermediate Map[K, List[V]]
  val pets = List("cat","dog","dog","bird","cat","dog")
  pets.groupMapReduce(identity)(_ => 1)(_ + _) // Map(cat -> 2, dog -> 3, bird -> 1) // More efficient than groupBy.map - avoids creating intermediate lists

@main
// 5) Lazy pipelines with view (no temps until forced)
def lazyPipelines(): Unit =
  val total =
    (1 to 1_000_000).view
      .map(_ * 2)
      .filter(_ % 3 == 0)
      .take(5)
      .sum                      // forces only 5 elements

@main
// 6) For-comprehension desugars to map/flatMap/withFilter
def forComprehension(): Unit =
  val res =
    for {
      n <- List(1,2,3,4) if n % 2 == 0
    } yield n * 10
  // same as: List(1,2,3,4).withFilter(_%2==0).map(_*10)