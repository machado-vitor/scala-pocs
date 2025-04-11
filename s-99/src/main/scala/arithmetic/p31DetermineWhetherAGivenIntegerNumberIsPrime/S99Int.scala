package arithmetic.p31DetermineWhetherAGivenIntegerNumberIsPrime

import arithmetic.p31DetermineWhetherAGivenIntegerNumberIsPrime.S99Int.primes
import arithmetic.p31DetermineWhetherAGivenIntegerNumberIsPrime.S99Int.intToS99Int

import scala.language.implicitConversions

class S99Int(val start: Int) {
  //P31
  def isPrime: Boolean =
    (start > 1) && primes.takeWhile {
      _ <= Math.sqrt(start)
    }.forall{ start % _ != 0 }
}

object S99Int {
  val primes: Seq[Int] = LazyList.cons(2, LazyList.from(3, 2) filter { _.isPrime })

  implicit def intToS99Int(n: Int): S99Int = new S99Int(n)
}

@main
def main(): Unit = {
  println(7.isPrime)
}