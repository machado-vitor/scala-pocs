package presentation

import scala.collection.mutable.{ArrayBuffer, ArrayDeque, HashMap}

@main
def builderFreeze(): Unit =
  val buf = ArrayBuffer[Int]()
  buf.sizeHint(100_000)            // avoid repeated growth
  (1 to 10).foreach(buf += _)
  val xs: Vector[Int] = buf.toVector // publish immutable

@main
def queueProcessing(): Unit =
  val q = ArrayDeque(1,2,3,4)
  while (q.nonEmpty) {
    val x = q.removeHead()         // O(1)
    println(s"Processed: $x, remaining queue: $q")
  }

@main
def memoizedFib(): (BigInt, Int) = {
  val memo = HashMap[Int, BigInt](0 -> 0, 1 -> 1)
  def fib(k: Int): BigInt = memo.getOrElseUpdate(k, fib(k - 1) + fib(k - 2)) // cache with getOrElseUpdate
  (fib(45), memo.size) // return value and cache size to show effect
}
