package presentation

import scala.collection.mutable.{ArrayBuffer, ArrayDeque, HashMap}

@main
// 1) Builder → Freeze (the pattern)
def builderFreeze(): Unit =
  val buf = ArrayBuffer[Int]()
  buf.sizeHint(100_000)            // avoid repeated growth
  (1 to 10).foreach(buf += _)
  val xs: Vector[Int] = buf.toVector // publish immutable

@main
// 2) Queue processing (tight loop)
def queueProcessing(): Unit =
  val q = ArrayDeque(1,2,3,4)
  while (q.nonEmpty) {
    val x = q.removeHead()         // O(1)
    println(s"Processed: $x, remaining queue: $q")
  }

@main
// 3) In-place counting (then freeze)
def inPlaceCounting(): Unit =
  val counts = HashMap.empty[String, Int]
  for (w <- List("a","b","a","c","a","b"))
    counts(w) = counts.getOrElse(w, 0) + 1

  val result: Map[String, Int] = counts.toMap // hand back immutable
  println(result) // Map(a -> 3, b -> 2, c -> 1)00000