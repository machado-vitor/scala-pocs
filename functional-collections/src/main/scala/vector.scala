// Vector has more evenly balanced access patterns than a list
// it is a tree with a branch out factor
// if 32 elements -> just an array.
// if more than 32 elements -> array of arrays.
// the tree grows up to 5 levels. 32x32 = 1024, 2^5x5 = 2^25 elements.

// Changing an element.
// it will create a new vector for that element that is being changed, and for their parents, until the root.

val fruits = Vector("Apple", "Orange", "Banana")
val nums = Vector(1, 2, 3)
// Vector has the same operations as lists, except for :: operation.
// Instead of x :: xs
// use x +: xs or xs :+ x
// vectors are symmetric, we don't have the advantage of lists to add elements to the left.

// Both List and Vector extend the supertrait Seq, which in turn extends the supertrait Iterable.
