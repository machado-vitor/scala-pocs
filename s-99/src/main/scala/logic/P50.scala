package logic

object P50 {

  sealed trait HuffmanTree
  case class Leaf(symbol: String, freq: Int) extends HuffmanTree
  case class Node(left: HuffmanTree, right: HuffmanTree, freq: Int) extends HuffmanTree

  def huffman(freqs: List[(String, Int)]): List[(String, String)] = {
    if (freqs.isEmpty) return List()
    if (freqs.length == 1) return List((freqs.head._1, "0"))

    val tree = buildHuffmanTree(freqs) // Build the Huffman tree

    generateCodes(tree, "") // Generate codes from the tree
  }

  private def buildHuffmanTree(freqs: List[(String, Int)]): HuffmanTree = {
    import scala.collection.mutable.PriorityQueue

    // Create initial leaves and add to priority queue (min-heap)
    implicit val ordering: Ordering[HuffmanTree] = Ordering.by[HuffmanTree, Int] {
      case Leaf(_, freq) => freq
      case Node(_, _, freq) => freq
    }.reverse

    val queue = PriorityQueue[HuffmanTree]()
    freqs.foreach { case (symbol, freq) => queue.enqueue(Leaf(symbol, freq)) }

    // Build tree by merging nodes with lowest frequencies
    while (queue.size > 1) {
      val right = queue.dequeue()
      val left = queue.dequeue()
      val mergedFreq = getFreq(left) + getFreq(right)
      queue.enqueue(Node(left, right, mergedFreq))
    }

    queue.dequeue()
  }

  private def getFreq(tree: HuffmanTree): Int = tree match {
    case Leaf(_, freq) => freq
    case Node(_, _, freq) => freq
  }

  private def generateCodes(tree: HuffmanTree, prefix: String): List[(String, String)] = {
    tree match {
      case Leaf(symbol, _) => List((symbol, if (prefix.isEmpty) "0" else prefix))
      case Node(left, right, _) =>
        generateCodes(left, prefix + "0") ::: generateCodes(right, prefix + "1")
    }
  }


  def main(args: Array[String]): Unit = {
    println("Huffman Coding Examples")
    println("=" * 40)

    // Example 1: Original problem example
    val example1 = List(("a", 45), ("b", 13), ("c", 12), ("d", 16), ("e", 9), ("f", 5))
    println(s"Example 1: $example1")
    val result1 = huffman(example1)
    println(s"Result: $result1")
    println()

    // Example 2: Simple case
    val example2 = List(("x", 3), ("y", 1))
    println(s"Example 2: $example2")
    val result2 = huffman(example2)
    println(s"Result: $result2")
    println()

    // Example 3: Single symbol
    val example3 = List(("z", 10))
    println(s"Example 3: $example3")
    val result3 = huffman(example3)
    println(s"Result: $result3")
    println()

    // Example 4: More balanced frequencies
    val example4 = List(("a", 10), ("b", 10), ("c", 10), ("d", 10))
    println(s"Example 4: $example4")
    val result4 = huffman(example4)
    println(s"Result: $result4")
    println()

    // Example 5: Text analysis example
    val example5 = List(("e", 12), ("t", 9), ("a", 8), ("o", 7), ("i", 6), ("n", 6), ("s", 4), ("h", 4), ("r", 4))
    println(s"Example 5 (common English letters): $example5")
    val result5 = huffman(example5)
    println(s"Result: $result5")
  }
}