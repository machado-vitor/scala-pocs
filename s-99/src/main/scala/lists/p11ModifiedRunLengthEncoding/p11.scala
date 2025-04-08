package lists.p11ModifiedRunLengthEncoding

//     scala> encodeModified(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
//     res0: List[Any] = List((4,'a), 'b, (2,'c), (2,'a), 'd, (4,'e))



@main
def main(): Unit = {
  println(encodeModified(List('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e')))
}

def encodeModified[A](ls: List[A]): List[Any] = {
  import lists.p10RunLengthEncodingOfAList.encode
  encode(ls) map { t => if (t._1 == 1) t._2 else t }
}