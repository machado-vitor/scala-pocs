// This is a binary tree data structure using Scala's ADT pattern.

package binarytree {

  // trait: supports multiple inheritance - intended for behavior and capabilities, weaker binary compatibility with java
  // and are better for mixins, and are good practice not hold state, it can cause conflict if other trait is used with same fields.
  // abstract class: only single inheritance - better for holding state
  sealed abstract class Tree[+T] {
    def isMirrorOf[V](tree: Tree[V]): Boolean
    def isSymmetric: Boolean
  }

  case class Node[+T](value: T, left: Tree[T] = End, right: Tree[T] = End) extends Tree[T] {
    override def isMirrorOf[V](tree: Tree[V]): Boolean = tree match {
      case t: Node[V] => left.isMirrorOf(t.right) && right.isMirrorOf(t.left)
      case _ => false
    }

    override def isSymmetric: Boolean = left.isMirrorOf(right)
    override def toString: String = "T(" + value.toString + " " + left.toString + " " + right.toString + ")"
  }

  case object End extends Tree[Nothing] {
    override def isMirrorOf[V](tree: Tree[V]): Boolean = tree == End
    override def isSymmetric: Boolean = true
    override def toString = "."
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
    // balanced binary trees with 4 nodes.
    // T(x T(x . .) T(x . T(x . .))) L subtree: 1 node, R subtree: 2 nodes
    // T(x T(x . T(x . .)) T(x . .)) L subtree: 2 nodes, R subtree: 1 node
    // T(x T(x . .) T(x T(x . .) .)) L subtree: 1 node, R subtree: 2 nodes
    // T(x T(x T(x . .) .) T(x . .)) L subtree: 2 nodes, R subtree: 1 node

    //P56
    println(Node('a', Node('b'), Node('c')).isSymmetric) // true
    println(Node('a', End, Node('c')).isSymmetric) // false
  }
}
