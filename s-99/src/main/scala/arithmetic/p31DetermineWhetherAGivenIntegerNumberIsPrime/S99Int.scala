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
  def primeFactors: List[Int] = // Define method to return prime factors of `start` as a List[Int]

    @tailrec // Tail-recursive annotation for optimization
    def primeFactorsR(n: Int, divisor: Int, acc: List[Int]): List[Int] =
      if n == 1 then acc.reverse // Base case: if n is 1, return accumulated factors (in correct order)
      else if n % divisor == 0 then // If current divisor divides n evenly
        primeFactorsR(n / divisor, divisor, divisor :: acc) // Add divisor to list and recurse with reduced n
      else
        primeFactorsR(n, nextDivisor(divisor), acc) // Otherwise, try the next possible divisor
    primeFactorsR(start, 2, Nil) // Kick off recursion with `start`, starting from divisor 2 and empty accumulator

  // P36: Return prime factors with their multiplicities as a List of tuples
  def primeFactorMultiplicity: List[(Int, Int)] =
    primeFactors.groupBy(identity).map { case (p, list) => (p, list.length) }.toList.sortBy(_._1)

  def primeFactorMultiplicityMap: Map[Int, Int] =
    primeFactors.groupBy(identity).view.mapValues(_.size).toMap

  // P37:
  def totientImproved: Int = {
    primeFactorMultiplicity
      .map { 
        case (p, m) => (p - 1) * BigInt(p).pow(m - 1).toInt
      }.product
  }


  private def nextDivisor(d: Int): Int = // Helper to get the next potential divisor
    if (d == 2) 3 else d + 2             // From 2 jump to 3, then only test odd numbers (skip even numbers > 2)
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
  println(s"Prime factors with multiplicity (list): ${315.primeFactorMultiplicity}")
  println(s"Prime factors with multiplicity (map): ${315.primeFactorMultiplicityMap}")
  println(s"Totient improved of 36: ${36.totientImproved}")
}
