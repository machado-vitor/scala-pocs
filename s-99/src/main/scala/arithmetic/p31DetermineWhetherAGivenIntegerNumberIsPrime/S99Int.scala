package arithmetic.p31DetermineWhetherAGivenIntegerNumberIsPrime

import scala.annotation.tailrec
import scala.language.implicitConversions

class S99Int(val start: Int) {
  import S99Int._
  //P31
  // while this is not using an explicit recursion,
  // this implementation have a form of implicit recursion.
  // The primes are generated lazily, so the list is not fully computed until needed.
  def isPrime: Boolean = {
    (start > 1) && primes.takeWhile {
      _ <= Math.sqrt(start) // we only check the primes up to the square root of `start`
    // if checking if 30 is prime, we only check 2, 3, 5, because root of 30 is 5.477
    }.forall{ start % _ != 0 } // check that none of the primes divides start evenly.
    // if any of the primes divides start evenly, then start is not prime.

  }

  //P33
  def isCoprimeTo(n: Int): Boolean = gcd(start, n) == 1 // Check if the GCD of `start` and `n` is 1

  //P34
  def totient: Int = (1 to start).count(start.isCoprimeTo(_)) // Count numbers from 1 to `start` that are coprime to `start`
  
  //P35
  def primeFactors: List[Int] = // Define method to return prime factors of `start` as a List[Int]
    @tailrec // Tail-recursive annotation for optimization
    def primeFactorsR(n: Int, divisor: Int, acc: List[Int]): List[Int] =
      if n == 1 then acc.reverse // Base case: if n is 1, return accumulated factors (in correct order)
      else if n % divisor == 0 then // If current divisor divides n evenly
        primeFactorsR(n / divisor, divisor, divisor :: acc) // Add divisor to list and recurse with reduced n
      else
        primeFactorsR(n, if (divisor == 2) 3 else divisor + 2, acc) // Otherwise, try the next possible divisor
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
  
  // // P38:
  //  // Compare the two methods of calculating Euler's totient function
    def totientComparison = {
      val startTime1 = System.currentTimeMillis()
      val result1 = start.totient
      val endTime1 = System.currentTimeMillis()
      
      val startTime2 = System.currentTimeMillis()
      val result2 = start.totientImproved
      val endTime2 = System.currentTimeMillis()
      
      val time1 = endTime1 - startTime1
      val time2 = endTime2 - startTime2
      println(s"Totient of 10090:")
      println(s"  Method 1 (naive): $result1 (took $time1 ms)")
      println(s"  Method 2 (improved): $result2 (took $time2 ms)")
      println(s"  The improved method was ${time1.toDouble / time2} times faster")
    }

  // P40 Goldbach’s conjecture says that every positive even number greater than 2 is the sum of two prime numbers.
  // E.g. 28 = 5 + 23 It is one of the most famous facts in number theory that has not been proved to be correct in the general case.
  // It has been numerically confirmed up to very large numbers (much larger than Scala’s Int can represent).
  // Write a function to find the two prime numbers that sum up to a given even integer.

  def goldbach: (Int, Int) = {
    primes.takeWhile(_ < start).find(p => (start - p).isPrime) match
      case Some(p) => (p, start - p)
      case None     => throw new IllegalArgumentException
  }   // From 2 jump to 3, then only test odd numbers (skip even numbers > 2)
}

// A companion object in Scala is a singleton object that has the same name as a class and is defined in the same file.
// It provides a way to associate static-like functionality with a class without using static members
object S99Int {
  implicit def intToS99Int(n: Int): S99Int = new S99Int(n) // Implicit conversion from Int to S99Int

  private val primes: Seq[Int] = LazyList.cons(2, LazyList.from(3, 2) filter { _.isPrime }) // which is the value of primes

  @tailrec
  def gcd(m: Int, n: Int): Int = if (n == 0) m else gcd(n, m % n) // this is not logically tied to S99Int start.
  //gcd(48, 18) calls gcd(18, 48 % 18) = gcd(18, 12)
  //gcd(18, 12) calls gcd(12, 18 % 12) = gcd(12, 6)
  //gcd(12, 6) calls gcd(6, 12 % 6) = gcd(6, 0)
  //gcd(6, 0) returns 6 (base case)

  def listPrimesInRange(r: Range): List[Int] = {
    @tailrec
    def primesInRange(n: Int, acc: List[Int]): List[Int] = {
      if (n > r.end) acc.reverse
      else if (n.isPrime) primesInRange(n + 1, n :: acc)
      else primesInRange(n + 1, acc)
    }

    primesInRange(r.start, Nil)
  }
}

@main
def main(): Unit = {
  import S99Int._
  println(s"7 is prime: ${30.isPrime}") // check how it works
  println(s"Greatest common divisor between 36 and 63: ${gcd(36, 63)}")
  println(s"Determining whether 35 and 64 are coprime: ${35.isCoprimeTo(64)}")
  println(s"Totient of 10: ${10.totient}")
  println(s"Prime factors of 315: ${315.primeFactors}")
  println(s"Prime factors with multiplicity (list): ${315.primeFactorMultiplicity}")
  println(s"Prime factors with multiplicity (map): ${315.primeFactorMultiplicityMap}")
  println(s"Totient improved of 36: ${36.totientImproved}")
  10090.totientComparison
  println(s"List of primes in range 10 to 50: ${listPrimesInRange(10 to 50)}")
//  println(s"Goldbach's conjecture for 28: ${28.goldbach}")

}
