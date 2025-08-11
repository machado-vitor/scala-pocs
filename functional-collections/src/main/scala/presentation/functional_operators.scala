package presentation

@main
def functionalOperators(): Unit = {
  val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 18, 20, 24)
  val words = List("scala", "java", "python", "kotlin", "clojure", "haskell")
  val xs = (1 to 100).toList

  val lazyResult = xs.view
    .map(_ * 2)           // Transform each item
    .filter(_ % 3 == 0)   // Select only multiples of three
    .take(5)              // Limit the stream
    .toList               // Finally materialize results

// transform operations
  val doubled = numbers.map(_ * 2)
  val uppercase = words.map(_.toUpperCase)

  val wordChars = words.flatMap(_.toCharArray)
  val numberRanges = List(1, 2, 3).flatMap(n => 1 to n)

  val evenSquares = numbers.collect {
    case n if n % 2 == 0 => n * n
  }
  val longWords = words.collect {
    case word if word.length > 5 => word.toUpperCase
  }


// select operations
  val evenNumbers = numbers.filter(_ % 2 == 0)
  val shortWords = words.filter(_.length <= 5)

  val hasLargeNumber = numbers.exists(_ > 20)
  val hasLongWord = words.exists(_.length > 6)

  val firstEven = numbers.find(_ % 2 == 0)
  val firstLongWord = words.find(_.length > 5)


// Combine
  val sum = numbers.foldLeft(0)(_ + _)
  val product = numbers.take(5).foldLeft(1)(_ * _)
  val concatenated = words.foldLeft("")((acc, word) => acc + word.capitalize + " ")

  val runningSums = numbers.take(8).scanLeft(0)(_ + _)
  val runningProducts = List(1, 2, 3, 4, 5).scanLeft(1)(_ * _)

  val numbersByParity = numbers.groupBy(_ % 2)
  val wordsByLength = words.groupBy(_.length)
}
