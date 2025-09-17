// Minimal test helper
object Spec {
  final case class Failed(name: String, cause: Throwable) extends RuntimeException(cause)
  def test(name: String)(body: => Unit): Unit = {
    try {
      body
      println(s"✓ $name")
    } catch {
      case t: Throwable =>
        println(s"✗ $name -> ${t.getMessage}")
        throw Failed(name, t)
    }
  }
  def approxEqual(a: Float, b: Float, eps: Float = 1e-6f): Unit =
    assert(math.abs(a - b) <= eps, s"$a not within $eps of $b")
}

