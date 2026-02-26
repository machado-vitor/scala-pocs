// This is a binary tree data structure using Scala's ADT pattern.

package binarytree {

  // trait: supports multiple inheritance - intended for behavior and capabilities, weaker binary compatibility with java
  // and are better for mixins, and are good practice not hold state, it can cause conflict if other trait is used with same fields.
  // abstract class: only single inheritance - better for holding state

  // A - Invariant (flows both ways (read-write))
  // If A is subtype of B, Inv[A] has no relationship with Inv[B]
  // Can't have subtyping relationship, even though the contained types might be related by inheritance.
  // it exists for safety, both consume and produce like:
  //class Box[A] {
  //  def get: A
  //  def put(a: A): Unit
  //}

  // +A - Covariant (only flows out (read-only))
  // If A is subtype of B, Cov[A] is subtype os Cov[B].
  // this is necessary for type safety reasons, because we could inject wrong subtypes.
  // that means that a type parameter +A must apper only in covariant positions (return types, never parameter types)

  // -A - Contravariant (only flows in (write-only))
  // Is A is subtype of B, Contra[B] is subtype of Contra[A]

  sealed abstract class Tree[+T] {
    def isMirrorOf[V](tree: Tree[V]): Boolean
    def isSymmetric: Boolean
    def addValue[U >: T: Ordering](x: U): Tree[U] // U must be a supertype of T
    def nodeCount: Int
    def leafCount: Int
    def leafList: List[T]
    def internalList: List[T]
    def layoutBinaryTree: Tree[T] = layoutBinaryTreeInternal(1, 1)._1
    def layoutBinaryTreeInternal(x: Int, depth: Int): (Tree[T], Int)
  }

  // Not a case class so that PositionedNode can extend it (case-to-case inheritance is prohibited in Scala 3).
  // Companion object below provides apply/unapply to preserve the same construction and pattern matching syntax.
  class Node[+T](val value: T, val left: Tree[T] = End, val right: Tree[T] = End) extends Tree[T] {
    override def isMirrorOf[V](tree: Tree[V]): Boolean = tree match { // V is invariant because it appears in a contravariant position (method parameter)
      case t: Node[V] => left.isMirrorOf(t.right) && right.isMirrorOf(t.left) // recursively check if left subtree mirrors the other's right AND right subtree mirrors the other's left
      case _ => false // if comparing against anything else (like End): return false.
    }

    override def isSymmetric: Boolean = left.isMirrorOf(right)

    override def addValue[U >: T: Ordering](x: U): Tree[U] = {
      val ord = summon[Ordering[U]]
      if (ord.lt(x, value)) Node(value, left.addValue(x), right)
      else Node(value, left, right.addValue(x))
    }

    override def nodeCount: Int = 1 + left.nodeCount + right.nodeCount

    override def leafCount: Int = (left, right) match {
      case (End, End) => 1 // this is a leaf (no successors)
      case _ => left.leafCount + right.leafCount // recursively count leaves in subtrees
    }

    override def leafList: List[T] = (left, right) match {
      case (End, End) => List(value) // this is a leaf, return its value
      case _ => left.leafList ::: right.leafList // recursively collect leaves from both subtrees
    }

    override def internalList: List[T] = (left, right) match {
      case (End, End) => Nil // leaf node, not internal
      case _ => value :: left.internalList ::: right.internalList
    }

    override def toString: String = s"T(${value.toString} ${left.toString} ${right.toString})"

    def layoutBinaryTreeInternal(x: Int, depth: Int): (Tree[T], Int) = {
      val (leftTree, myX) = left.layoutBinaryTreeInternal(x, depth + 1)
      val (rightTree, nextX) = right.layoutBinaryTreeInternal(myX + 1, depth + 1)
      (PositionedNode(value, leftTree, rightTree, myX, depth), nextX)
    }
  }

  object Node {
    def apply[T](value: T, left: Tree[T] = End, right: Tree[T] = End): Node[T] = new Node(value, left, right)
    def unapply[T](n: Node[T]): Some[(T, Tree[T], Tree[T])] = Some((n.value, n.left, n.right))
  }

  case class PositionedNode[+T](override val value: T, override val left: Tree[T], override val right: Tree[T], x: Int, y: Int) extends Node[T](value, left, right) {
    override def toString: String = "T[" + x.toString + "," + y.toString + "](" + value.toString + " " + left.toString + " " + right.toString + ")"
  }

  case object End extends Tree[Nothing] {
    override def isMirrorOf[V](tree: Tree[V]): Boolean = tree == End
    override def isSymmetric: Boolean = true
    override def addValue[U: Ordering](x: U): Tree[U] = Node(x)
    override def nodeCount: Int = 0
    override def leafCount: Int = 0
    override def leafList: List[Nothing] = Nil
    override def internalList: List[Nothing] = Nil
    override def toString = "."
    def layoutBinaryTreeInternal(x: Int, depth: Int): (Tree[Nothing], Int) = (End, x)
  }

  // P55
  object Tree {
    // this generates all combinations of valid left/right subtrees.
    def cBalanced[T](n: Int, value: T): List[Tree[T]] = n match {
        case 0 => List(End)
        case _ =>
          val subtreeNodes = n - 1 // 1 node is the root, so we remove it, the rest go to children
          val smallerHalf = subtreeNodes / 2
          val largerHalf = subtreeNodes - smallerHalf
          if (smallerHalf == largerHalf) {
            // for is a syntax sugar for chaining operations on monads
            for {
              left <- cBalanced(smallerHalf, value)
              right <- cBalanced(smallerHalf, value)
            } yield Node(value, left, right) // yield rewraps the result
            // it means: Return this value, wrapped in the same context
          } // resolve, balanced subtrees, as for n = 3
          else
            for {
              left <- cBalanced(smallerHalf, value)
              right <- cBalanced(largerHalf, value)
              (l, r) <- List((left, right), (right, left)) // unbalanced subtrees, for n = 4 for example, generate both sides.
            } yield Node(value, l, r)
    }

    // P58 Symmetric + completely balanced trees with n nodes.
    def symmetricBalancedTrees[T](n: Int, value: T): List[Node[T]] =
      cBalanced(n, value)
        .filter(_.isSymmetric) // keep only the trees where left subtree is a mirror of the right subtree.
        .collect { case t: Node[T] => t } // narrows  type from Tree[T] to Node[T]

    // h < 0 -> no trees possible
    // h == 0 -> just End (empty tree, height 0)
    // h == 1 -> a single leaf Node(value) (height 1)
    def hbalTrees[T](h: Int, value: T): List[Tree[T]] = h match { // it genarates all height ;balanced binary trees of exaclty height h.
      case n if n < 0 =>
        Nil
      case 0 =>
        List(End)
      case 1 =>
        List(Node(value))
      case _ =>
        val t1 = hbalTrees(h - 1, value) // height h-1
        val t2 = hbalTrees(h - 2, value) // height h-2
        val same =
          for {
            l <- t1
            r <- t1
          } yield Node(value, l, r)
        val leftHigh =
          for {
            l <- t1
            r <- t2
          } yield Node(value, l, r)
        val rightHigh =
          for {
            l <- t2
            r <- t1
          } yield Node(value, l, r)
        same ++ leftHigh ++ rightHigh
    }

    def minHbalNodes(h: Int): Int = h match {
      case n if n < 1 => 0
      case 1 => 1
      case _ => 1 + minHbalNodes(h - 1) + minHbalNodes(h - 2)
    }

    def maxHbalHeight(n: Int): Int = {
      LazyList.from(1) // → 1, 2, 3, 4, 5, 6, ...
        .takeWhile(minHbalNodes(_) <= n) // → keep heights while min nodes needed ≤ n
        .last // → grab the last (biggest) valid height
      // lazylist are only computed when accessed. once an element is computed, it's cached.
      // that's the different from an Iterator, which is use-once.
    }

    def hbalTreesWithNodes[T](n: Int, value: T): List[Node[T]] = {
      val minH = if (n == 0) 0 else (math.log(n) / math.log(2) + 1).toInt
      val maxH = maxHbalHeight(n)
      (minH to maxH).flatMap(hbalTrees(_, value)).collect {
        case t: Node[T] if t.nodeCount == n => t
      }.toList
    }

    // P63 Complete binary tree.
    // Uses heap addressing: node at address a has children at 2*a and 2*a+1.
    // A node exists if its address <= n.
    def completeBinaryTree[T](n: Int, value: T): Tree[T] = {
      def generate(addr: Int): Tree[T] =
        if (addr > n) End
        else Node(value, generate(2 * addr), generate(2 * addr + 1))
      generate(1)
    }

    def fromList[T: Ordering](list: List[T]): Tree[T] =
      list.foldLeft(End: Tree[T])((tree, value) => tree.addValue(value))
  }

  object Tree1 extends App {
    val tree = Node("a", Node("b", Node("d"), Node("e")), Node("c", End, Node("f", Node("g"), End)))
    println(tree)

    Tree.cBalanced(4, "x").foreach(println)
    // balanced binary trees with 4 nodes. 4 balanced trees
    // T(x T(x . .) T(x . T(x . .))) L subtree: 1 node, R subtree: 2 nodes
    // T(x T(x . T(x . .)) T(x . .)) L subtree: 2 nodes, R subtree: 1 node
    // T(x T(x . .) T(x T(x . .) .)) L subtree: 1 node, R subtree: 2 nodes
    // T(x T(x T(x . .) .) T(x . .)) L subtree: 2 nodes, R subtree: 1 node

    //P56
    println(Node('a', Node('b'), Node('c')).isSymmetric) // true
    println(Node('a', End, Node('c')).isSymmetric) // false

    //P57
    println(End.addValue(2)) // T(2 . .)
    println(End.addValue(2).addValue(3)) // T(2 . T(3 . .))
    println(End.addValue(2).addValue(3).addValue(0)) // T(2 T(0 . .) T(3 . .))
    println(Tree.fromList(List(3, 2, 5, 7, 1))) // T(3 T(2 T(1 . .) .) T(5 . T(7 . .)))
    println(Tree.fromList(List(5, 3, 18, 1, 4, 12, 21)).isSymmetric) // true
    println(Tree.fromList(List(3, 2, 5, 7, 4)).isSymmetric) // false

    // P58
    println(Tree.symmetricBalancedTrees(5, "x").size) // 2
    println(Tree.symmetricBalancedTrees(7, "x").size) // 1
    println(Tree.symmetricBalancedTrees(9, "x").size) // 4

    Tree.symmetricBalancedTrees(5, "x").foreach(println)

    // P59
    println(Tree.hbalTrees(3, "x").size) // 15
    Tree.hbalTrees(3, "x").foreach(println)

    // P60
    println(Tree.minHbalNodes(3)) // 4
    println(Tree.maxHbalHeight(4)) // 3
    Tree.hbalTreesWithNodes(4, "x").foreach(println)
    println(Tree.hbalTreesWithNodes(15, "x").size) // 1553

    // P61 leafCount
    println(Node('x', Node('x'), End).leafCount) // 1
    println(Node('a', Node('b'), Node('c')).leafCount) // 2
    println(tree.leafCount) // 3 (leaves are: d, e, g)

    // P61A leafList
    println(Node('a', Node('b'), Node('c', Node('d'), Node('e'))).leafList) // List(b, d, e)
    println(tree.leafList) // List(d, e, g)

    // P62 internalList
    println(Node('a', Node('b'), Node('c', Node('d'), Node('e'))).internalList) // List(a, c)

    // P63 completeBinaryTree
    println(Tree.completeBinaryTree(6, "x")) // T(x T(x T(x . .) T(x . .)) T(x T(x . .) .))

    // P64
    println(Node('a', Node('b', End, Node('c')), Node('d')).layoutBinaryTree)
    // Visually, x is the column and y is the row (depth):
    //
    //      x: 1  2  3  4
    //  y: 1         a
    //  y: 2   b        d
    //  y: 3      c
  }
}

// Complete Binary Tree

// What is an address?
// it is a number we assign for each position in the three, going level by level, left to right.
// like for example:
//  Level 1:          1
//                  /   \
//  Level 2:       2     3
//                / \   / \
//  Level 3:     4   5 6   7
// This is called levelorder numbering. The root gets 1, then we count left-to-right on each level.
// -----------------------------------------------------------------------------------------------
// Why 2*a and 2*a+1?
// this is a mathematical property of this numbering.
// Look at the pattern:
//  Node 1 → children: 2, 3    (2*1, 2*1+1)
//  Node 2 → children: 4, 5    (2*2, 2*2+1)
//  Node 3 → children: 6, 7    (2*3, 2*3+1)
//  Node 4 → children: 8, 9    (2*4, 2*4+1)
// This is how Binary heaps (priority queues) stores a tree in a flat array.
// It works because each level has exactly twice as many positions as the previous one. Lvl 1 = 1, 2 = 2, 3 = 4...
// So when we go down one level, addresses double, left child is 2*a and right child is 2*a+1.

// Why does a node exist only if addr <= n?
// because anything beyond n is a position that doesn't exist in out tree. the addresses are assigned sequentially from 1 to n

// Why is the tree shaped like this?
// The left-adjusted rule is what makes a complete binary tree special.
// When the last level isn't full, all nodes pack to the left.


// layoutBinaryTree, explanation
// The goal is to assign (x, y) coordinates to every node using inorder position for x and depth for y.
// The leftmost node in inorder gets x=1, the next gets x=2, etc. Root is at depth 1.

//Inorder is one of three classic ways to traverse (visit) a binary tree. The order is: left subtree
//  → current node → right subtree.
//
//  The three traversals:
//  - Preorder: current → left → right
//  - Inorder: left → current → right
//  - Postorder: left → right → current
//
//  Example:
//          a
//         / \
//        b   d
//         \
//          c
//
//  - Preorder: a, b, c, d
//  - Inorder: b, c, a, d
//  - Postorder: c, b, d, a