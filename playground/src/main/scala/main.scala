@main
def main(): Unit = {
  val name = "Vitor"
  val stringContext = StringContext("Hello, ", "").s(name)
  println(stringContext)

  // in this case, s"..." is compiled using FastStringInterpolator::interpolateS via fasttrack.
  println(s"Hello, $name")
}