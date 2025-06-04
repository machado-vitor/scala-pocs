import scala.annotation.tailrec

@main
def tailRecDemo(): Unit = {
  // Basic factorial examples
  println("=== Factorial Examples ===")
  
  // Non-tail recursive factorial
  def factorialNonTailRec(n: Int): BigInt = {
    if (n <= 1) 1
    else n * factorialNonTailRec(n - 1) 
    // Not tail recursive because after the recursive call returns,
    // we still need to multiply by n
  }
  
  // Tail recursive factorial
  def factorialTailRec(n: Int): BigInt = {
    @tailrec
    def loop(n: Int, acc: BigInt): BigInt = {
      if (n <= 1) acc
      else loop(n - 1, n * acc) 
      // Tail recursive: the recursive call is the last operation
    }
    
    loop(n, 1)
  }
  
  // Compare results for small inputs
  println(s"Non-tail recursive factorial of 5: ${factorialNonTailRec(5)}")
  println(s"Tail recursive factorial of 5: ${factorialTailRec(5)}")
  
  // Try with larger numbers - non-tail recursive version might cause stack overflow
  try {
    println(s"Tail recursive factorial of 100000: ${factorialTailRec(100000)}")
    // This would likely cause a stack overflow with the non-tail recursive version
//     println(s"Non-tail recursive factorial of 100000: ${factorialNonTailRec(100000)}")
  } catch {
    case e: StackOverflowError => println("Stack overflow occurred!")
  }
  
  // === Fibonacci Example ===
  println("\n=== Fibonacci Example ===")
  
  // Tail recursive Fibonacci
  def fibonacciTailRec(n: Int): BigInt = {
    @tailrec
    def fibLoop(i: Int, a: BigInt, b: BigInt): BigInt = {
      if (i == n) a
      else fibLoop(i + 1, b, a + b)
    }
    
    if (n <= 0) 0
    else fibLoop(0, 0, 1)
  }
  
  // Generate first 10 Fibonacci numbers
  println("First 10 Fibonacci numbers:")
  for (i <- 0 until 10) {
    println(s"Fibonacci($i) = ${fibonacciTailRec(i)}")
  }
  
  // === List Processing Example ===
  println("\n=== List Processing Example ===")
  
  @tailrec
  def sum(list: List[Int], acc: Int = 0): Int = list match {
    case Nil => acc
    case head :: tail => sum(tail, acc + head)
  }
  
  val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  println(s"Sum of $numbers is ${sum(numbers)}")
}
