package arithmetic.p31DetermineWhetherAGivenIntegerNumberIsPrime

import scala.annotation.tailrec
import scala.language.implicitConversions

class S99Int(val start: Int) {
  import S99Int._
  //P31
  def isPrime: Boolean =
    (start > 1) && primes.takeWhile {
      _ <= Math.sqrt(start)
    }.forall{ start % _ != 0 }

  //P33
  def isCoprimeTo(n: Int): Boolean = gcd(start, n) == 1

  //P34
  def totient: Int = (1 to start).count(start.isCoprimeTo(_))
  
  //P35
  def primeFactors: List[Int] =
    def primeFactorsR(n: Int, divisor: Int, acc: List[Int]): List[Int] =
      if n == 1 then acc.reverse
      else if n % divisor == 0 then primeFactorsR(n / divisor, divisor, divisor :: acc)
      else primeFactorsR(n, nextDivisor(divisor), acc)

    primeFactorsR(start, 2, Nil)

  private def nextDivisor(d: Int): Int = if (d == 2) 3 else d + 2
}

object S99Int {
  val primes: Seq[Int] = LazyList.cons(2, LazyList.from(3, 2) filter { _.isPrime })

  @tailrec
  def gcd(m: Int, n: Int): Int = if (n == 0) m else gcd(n, m % n)


  implicit def intToS99Int(n: Int): S99Int = new S99Int(n)
}

@main
def main(): Unit = {
  import S99Int._
  println(s"7 is prime: ${7.isPrime}")
  println(s"Greatest common divisor between 36 and 63: ${gcd(36, 63)}")
  println(s"Determining whether 35 and 64 are coprime: ${35.isCoprimeTo(64)}")
  println(s"Totient of 10: ${10.totient}")
  println(s"Prime factors of 315: ${315.primeFactors}")
}