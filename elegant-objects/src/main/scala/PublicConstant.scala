object ConstantsBad {
  val CrLf: String = "\r\n"
}

class RowsBad2(private val all: List[String]) {
  def print(p: java.io.PrintStream): Unit = {
    all.foreach { row => p.printf("{%s}%s", row, ConstantsBad.CrLf) }
  }
}


class Rows2(private val all: List[String]) {
  private val CrLf: String = "\r\n"
  def print(p: java.io.PrintStream): Unit =
    all.foreach { row => p.printf("{%s}%s", row, CrLf) }
}

trait LineSeparator { def value: String }
class CrLf extends LineSeparator { val value = "\r\n" }
class Lf extends LineSeparator { val value = "\n" }

class Records2(private val all: List[String], private val sep: LineSeparator) {
  def write(out: java.io.Writer): Unit = {
    all.foreach { rec =>
      out.write(rec)
      out.write(sep.value)
    }
  }
}


@main
def public_constants(): Unit = {
  val rows = List("a", "b", "c")

  val out: java.io.PrintStream = System.out
  RowsBad2(rows).print(out)
}