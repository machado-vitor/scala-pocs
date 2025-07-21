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
}
