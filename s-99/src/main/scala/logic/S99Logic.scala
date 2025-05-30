package logic

// P46: Truth tables for logical expressions.

class S99Logic(a: Boolean) {
  import S99Logic._

  def not(): Boolean = a match {
    case true => false
    case false => true
  }
  def and(a: Boolean, b: Boolean): Boolean = (a, b) match {
    case (true, true) => true
    case _ => false
  }

  def or(a: Boolean, b: Boolean): Boolean = (a, b) match {
    case (true, _) => true
    case (_, true) => true
    case _ => false
  }

  def equ(a: Boolean, b: Boolean): Boolean = or(and(a, b), and(not(a), not(b)))
  def xor(a: Boolean, b: Boolean): Boolean = not(equ(a, b))
  def nor(a: Boolean, b: Boolean): Boolean = not(or(a, b))
  def nand(a: Boolean, b: Boolean): Boolean = not(and(a, b))
  def impl(a: Boolean, b: Boolean): Boolean = or(not(a), b)
}

@main
def main(): Unit = {
  import S99Logic._

  println(not(true)) // false
  println(not(false)) // true
  println(and(true, true)) // true
  println(and(true, false)) // false
  println(or(true, false)) // true
  println(or(true, true)) // true
  println(or(false, false)) // false
  println(equ(true, true)) // true
  println(equ(true, false)) // false
  println(equ(false, false)) // true
  println(xor(true, true)) // false
  println(xor(true, false)) // true
  println(nor(true, false)) // false
  println(nor(false, false)) // true
  println(nand(true, true)) // false
  println(nand(true, false)) // true
  println(impl(true, true)) // true
  println(impl(true, false)) // false
  println(impl(false, true)) // true
  println(impl(false, false)) // true
}
