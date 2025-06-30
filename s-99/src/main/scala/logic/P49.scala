package logic
import scala.collection.mutable

object P49 {
  def gray(n: Int): List[String] =
    if (n == 0) List("")
    else { val lower = gray(n - 1)
      (lower map { "0" + _ }) ::: (lower.reverse map { "1" + _ })
    }

  private val strings = mutable.Map(0 -> List(""))
  def grayMemoized(n: Int): List[String] = {
    if (!strings.contains(n)) {
      strings(n) = (grayMemoized(n - 1) map { "0" + _ }) :::
                   (grayMemoized(n - 1).reverse map { "1" + _ })
    }
    strings(n)
  }

  def testGray(): Unit = {
    assert(gray(0) == List(""))
    assert(gray(1) == List("0", "1"))
    assert(gray(2) == List("00", "01", "11", "10"))
    assert(gray(3) == List("000", "001", "011", "010", "110", "111", "101", "100"))
    println("gray tests passed")
  }

  def testGrayMemoized(): Unit = {
    assert(grayMemoized(0) == List(""))
    assert(grayMemoized(1) == List("0", "1"))
    assert(grayMemoized(2) == List("00", "01", "11", "10"))
    assert(grayMemoized(3) == List("000", "001", "011", "010", "110", "111", "101", "100"))
    println("grayMemoized tests passed")
  }

  def main(args: Array[String]): Unit = {
    testGray()
    testGrayMemoized()
  }
}
