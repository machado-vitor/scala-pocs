// This is a binary tree data structure using Scala's ADT pattern.

package binarytree {

  sealed abstract class Tree[+T]

  case class Node[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T] {
    override def toString: String = "T(" + value.toString + " " + left.toString + " " + right.toString + ")"
  }

  case object End extends Tree[Nothing] {
    override def toString = "."
  }

  object Node {
    def apply[T](value: T): Node[T] = Node(value, End, End)
  }

  // P55
  object Tree {
    // this generates all combinations of valid left/right subtrees.
    def cBalanced[T](n: Int, value: T): List[Tree[T]] = {
      n match {
        case 0 => List(End)
        case _ =>
          val subtreeNodes = n - 1 // 1 node is the root, so we remove it, the rest go to children
          val smallerHalf = subtreeNodes / 2
          val largerHalf = subtreeNodes - smallerHalf
          if (smallerHalf == largerHalf)
            for {
              left <- cBalanced(smallerHalf, value)
              right <- cBalanced(smallerHalf, value)
            } yield Node(value, left, right) // resolve, balanced subtrees, as for n = 3
          else
            for {
              left <- cBalanced(smallerHalf, value)
              right <- cBalanced(largerHalf, value)
              (l, r) <- List((left, right), (right, left)) // unbalanced subtrees, for n = 4 for example, generate both sides.
            } yield Node(value, l, r)
      }
    }
  }

  object Tree1 extends App {
    val tree = Node("a", Node("b", Node("d"), Node("e")), Node("c", End, Node("f", Node("g"), End)))
    println(tree)

    Tree.cBalanced(4, "x").foreach(println)
  }
}
