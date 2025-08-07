

@main
def lazy_view(): Unit =
  // Lazy pipeline: nothing happens until forced
  val pipeline = (1 to 1_000_000).view
    .map(_ * 2)
    .filter(_ % 3 == 0)
    .take(5)

  // Force only minimal work:
  val forced = pipeline.toList
  println(s"lazy pipeline forced -> $forced")

  // Iterator: also lazy, single-pass
  val it = Iterator.from(1).map(_ * 3).filter(_ % 2 == 0).take(4)
  println(s"iterator example     -> ${it.toList}")

  // LazyList: potentially infinite, memoized
  lazy val fibs: LazyList[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map { case (a, b) => a + b }
  println(s"fibs take 8          -> ${fibs.take(8).toList}")