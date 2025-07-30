// operations
// xs.exists(p)
// xs.forall(p)
// xs.zip(ys)
// xs.unzip
// xs.flatmap(f)
// xs.sum
// xs.product
// xs.max
// xs.min

@main
def sequences(): Unit = {
  val xs =  Seq("a", "b", "c")
  val ys =  Seq("a", "a", "a")
  println(xs.exists(p => p == "a"))
  println(ys.forall(p => p == "a"))
  val xys = xs.zip(ys)
  println(xys) // a sequence of pairs from xs and ys // List((a,a), (b,a), (c,a))
  println(xys.unzip) // splits the sequence of pairs // (List(a, b, c),List(a, a, a))
  val xsUpperCase = xs.flatMap(char => Seq(char, char.toUpperCase))
  println(xsUpperCase) // List(a, A, b, B, c, C)
}
