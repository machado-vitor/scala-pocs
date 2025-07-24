import scala.annotation.tailrec


// Lists are linear.
// access to the first element is much faster than access to the middle or end of a list.
val fruits = List("Apple", "Orange", "Banana")
val nums = 1 :: 2 :: Nil
// same as 1 :: (2 :: Nul)
// 2 :: Nil created a list with a single element: List(2)
// 1 :: List(2) -> prepends 1 to the front -> List(1, 2)

@main
def lists(): Unit = {
  println(fruits.head) // "Apple"
  println(nums.tail) // 2 :: Nil
  println(nums.isEmpty) // false

  nums match {
    case x :: y :: _ => println(x + y) // 3
  }

  println(s"last: ${(fruits.last)}")
  println(s"head: ${(fruits.head)}")
}

//complexity of head is constant time.
// last takes steps proportional to the length of the list xs.
// init is analogous to last
@tailrec
def last[T](xs: List[T]): T = xs match {
  case List() => throw Error("last of empty list")
  case List(x) => x
  case y :: ys => last(ys)
}
