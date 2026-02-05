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
  }

  case class Node[+T](value: T, left: Tree[T] = End, right: Tree[T] = End) extends Tree[T] {
    override def isMirrorOf[V](tree: Tree[V]): Boolean = tree match {
      case t: Node[V] => left.isMirrorOf(t.right) && right.isMirrorOf(t.left)
      case _ => false
    }

    override def isSymmetric: Boolean = left.isMirrorOf(right)

    override def addValue[U >: T: Ordering](x: U): Tree[U] = {
      val ord = summon[Ordering[U]]
      if (ord.lt(x, value)) Node(value, left.addValue(x), right)
      else Node(value, left, right.addValue(x))
    }

    override def nodeCount: Int = 1 + left.nodeCount + right.nodeCount

    override def toString: String = s"T(${value.toString} ${left.toString} ${right.toString})"
  }

  case object End extends Tree[Nothing] {
    override def isMirrorOf[V](tree: Tree[V]): Boolean = tree == End
    override def isSymmetric: Boolean = true
    override def addValue[U: Ordering](x: U): Tree[U] = Node(x)
    override def nodeCount: Int = 0
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
      LazyList.from(1).takeWhile(minHbalNodes(_) <= n).last
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

    def fromList[T: Ordering](list: List[T]): Tree[T] =
      list.foldLeft(End: Tree[T])((tree, value) => tree.addValue(value))
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
  }
}
