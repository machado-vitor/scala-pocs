package presentation

@main
// 1) Transform: map, flatMap, collect
def transformOps(): Unit =
  val words = List("hi", "scala", "io")
  // map: 1-to-1
  println(words.map(_.length)) // List(2,5,2)
  // flatMap: 1-to-many
  println(words.flatMap(_.toCharArray).distinct) // List(h, i, s, c, a, l, o)
  // collect: filter + map in one pass
  val nums: List[Any] = List(1, "x", 2, 3.0, 4)
  nums.collect { case i: Int if i % 2 == 0 => i * 10 } // List(20, 40)


@main
// 2) Select: filter, partition, find
def selectOps(): Unit =
  val xs = Vector.range(1, 10)
  println(xs.filter(_ % 2 == 0)) // Vector(2,4,6,8)
  println(xs.partition(_ < 5)) // (Vector(1,2,3,4),Vector(5,6,7,8,9))
  println(xs.find(_ > 7)) // Some(8)

@main
// 3) Combine: foldLeft (safe), scan (running totals)
def combineOps(): Unit =
  val ys = List(1, 2, 3, 4)
  println(ys.foldLeft(0)(_ + _)) // 10
  println(ys.scanLeft(0)(_ + _)) // List(0,1,3,6,10)  // running sums

@main
// 4) Grouping done right: groupMapReduce (Scala 2.13+)
def grouping(): Unit =
  val pets = List("cat","dog","dog","bird","cat","dog")
  pets.groupMapReduce(identity)(_ => 1)(_ + _) // Map(cat -> 2, dog -> 3, bird -> 1)

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