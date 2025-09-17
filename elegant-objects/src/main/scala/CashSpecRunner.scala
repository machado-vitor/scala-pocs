import Spec.*

object CashSpecRunner {
  def main(args: Array[String]): Unit = {
    // Happy path
    test("multiplies amount by an integer factor") {
      val price = DefaultCash(2.0f)
      val triple = price.multiply(3)
      approxEqual(triple.amount, 6.0f)
    }

    // Edge cases
    test("multiplies by zero yields zero") {
      val any = DefaultCash(123.45f)
      approxEqual(any.multiply(0).amount, 0.0f)
    }

    test("multiplies by negative factor flips sign") {
      val debt = DefaultCash(10.0f)
      approxEqual(debt.multiply(-2).amount, -20.0f)
    }
  }
}

