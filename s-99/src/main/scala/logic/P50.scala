package logic

import scala.collection.mutable

object P50 {

  sealed trait HuffmanTree
  case class Leaf(symbol: String, freq: Int) extends HuffmanTree // Leaf nodes: Original symbols from input
  case class Node(left: HuffmanTree, right: HuffmanTree, freq: Int) extends HuffmanTree // Internal nodes: Combination of two Huffman trees with their combined frequency

  def huffman(freqs: List[(String, Int)]): List[(String, String)] = {
    if (freqs.isEmpty) return List()
    if (freqs.length == 1) return List((freqs.head._1, "0"))

    val tree = buildHuffmanTree(freqs) // Build the Huffman tree

    generateCodes(tree, "") // Generate codes from the tree
  }

  private def buildHuffmanTree(freqs: List[(String, Int)]): HuffmanTree = {

    // Define an ordering for the priority queue to create a min-heap based on frequency.
    // The `.reverse` is needed because PriorityQueue is a max-heap by default.
    implicit val ordering: Ordering[HuffmanTree] = Ordering.by[HuffmanTree, Int] {
      case Leaf(_, freq)   => freq
      case Node(_, _, freq) => freq
    }.reverse

    // Initialize the priority queue with a Leaf for each symbol.
    val queue = mutable.PriorityQueue[HuffmanTree]()
    freqs.foreach { case (symbol, freq) => queue.enqueue(Leaf(symbol, freq)) }

    // Iteratively build the tree by merging the two nodes with the lowest frequencies.
    while (queue.size > 1) {
      val right = queue.dequeue() // Node with the smallest frequency
      val left  = queue.dequeue() // Node with the second smallest frequency
      val mergedFreq = getFreq(left) + getFreq(right)
      // Create a new internal node and add it back to the queue.
      queue.enqueue(Node(left, right, mergedFreq)) // Create a new node that combines the two smallest nodes
    }

    // The last remaining element in the queue is the root of the Huffman tree.
    queue.dequeue() // Return the root of the Huffman tree
  }

  /**
   * Helper function to get the frequency from a HuffmanTree node.
   */
  private def getFreq(tree: HuffmanTree): Int = tree match {
    case Leaf(_, freq)      => freq
    case Node(_, _, freq) => freq
  }

  /**
   * Recursively traverses the Huffman tree to generate the codes for each symbol.
   *
   * @param tree   The Huffman tree (or subtree) to traverse.
   * @param prefix The Huffman code prefix accumulated so far.
   * @return A list of (symbol, huffman_code) pairs.
   */
  private def generateCodes(tree: HuffmanTree, prefix: String): List[(String, String)] = {
    tree match {
      // When a leaf is reached, the code for that symbol is the accumulated prefix.
      case Leaf(symbol, _) => List((symbol, if (prefix.isEmpty) "0" else prefix))
      // For an internal node, recursively traverse left and right.
      // Append '0' for the left branch and '1' for the right branch.
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
//
//    // Example 2: Simple case
//    val example2 = List(("x", 3), ("y", 1))
//    println(s"Example 2: $example2")
//    val result2 = huffman(example2)
//    println(s"Result: $result2")
//    println()
//
//    // Example 3: Single symbol
//    val example3 = List(("z", 10))
//    println(s"Example 3: $example3")
//    val result3 = huffman(example3)
//    println(s"Result: $result3")
//    println()
//
//    // Example 4: More balanced frequencies
//    val example4 = List(("a", 10), ("b", 10), ("c", 10), ("d", 10))
//    println(s"Example 4: $example4")
//    val result4 = huffman(example4)
//    println(s"Result: $result4")
//    println()
//
//    // Example 5: Text analysis example
//    val example5 = List(("e", 12), ("t", 9), ("a", 8), ("o", 7), ("i", 6), ("n", 6), ("s", 4), ("h", 4), ("r", 4))
//    println(s"Example 5 (common English letters): $example5")
//    val result5 = huffman(example5)
//    println(s"Result: $result5")
  }
}