import org.scalatest.funsuite.AnyFunSuite

final class CashTest extends AnyFunSuite {
  test("multiplies amount by an integer factor") {
    val price = DefaultCash(2.0f)
    val triple = price.multiply(3)
    assert(math.abs(triple.amount - 6.0f) <= 1e-6f)
  }

  test("multiplies by zero yields zero") {
    val any = DefaultCash(123.45f)
    assert(math.abs(any.multiply(0).amount - 0.0f) <= 1e-6f)
  }

  test("multiplies by negative factor flips sign") {
    val debt = DefaultCash(10.0f)
    assert(math.abs(debt.multiply(-2).amount - (-20.0f)) <= 1e-6f)
  }
}

