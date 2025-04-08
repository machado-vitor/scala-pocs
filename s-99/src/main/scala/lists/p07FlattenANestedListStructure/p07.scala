package lists.p07FlattenANestedListStructure

//    scala > flatten(List(List(1, 1), 2, List(3, List(5, 8))))
//     res0: List[Any] = List(1, 1, 2, 3, 5, 8)
@main
def main(): Unit = {
  println("flatten(List(List(1, 1), 2, List(3, List(5, 8))))")
  println(flatten(List(List(1, 1), 2, List(3, List(5, 8)))))
}

def flatten(ls: List[Any]): List[Any] = ls flatMap {
  case ms: List[_] => flatten(ms) // is the element is a list, call flatten recursively
  case e => List(e) // if not, return the element as a list
}