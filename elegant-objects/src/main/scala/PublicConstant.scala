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

  println("=== RowsBad2 (uses global ConstantsBad) ===")
  new RowsBad2(rows).print(out)

  println("\n=== Rows2 (uses private constant) ===")
  new Rows2(rows).print(out)

  println("\n=== Records2 with CRLF ===")
  val writer1 = new java.io.OutputStreamWriter(System.out)
  new Records2(rows, new CrLf).write(writer1)
  writer1.flush()

  println("\n=== Records2 with LF ===")
  val writer2 = new java.io.OutputStreamWriter(System.out)
  new Records2(rows, new Lf).write(writer2)
  writer2.flush()
}