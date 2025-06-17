package logic

import scala.language.implicitConversions

// P46: Truth tables for logical expressions.

class S99Logic(a: Boolean) {
  def not(): Boolean = a match {
    case true => false
    case false => true
  }

  def and(b: Boolean): Boolean = (a, b) match {
    case (true, true) => true
    case _ => false
  }

  def or(b: Boolean): Boolean = (a, b) match {
    case (true, _) => true
    case (_, true) => true
    case _ => false
  }

  def equ(b: Boolean): Boolean = (a && b) || (!a && !b)
  def xor(b: Boolean): Boolean = !equ(b)
  def nor(b: Boolean): Boolean = !or(b)
  def nand(b: Boolean): Boolean = !and(b)
  def impl(b: Boolean): Boolean = !a || b
}

object S99Logic {
  implicit def boolean2S99Logic(a: Boolean): S99Logic = new S99Logic(a)

  def not(a: Boolean): Boolean = a.not()
  def and(a: Boolean, b: Boolean): Boolean = a.and(b)
  def or(a: Boolean, b: Boolean): Boolean = a.or(b)
  def equ(a: Boolean, b: Boolean): Boolean = a.equ(b)
  def xor(a: Boolean, b: Boolean): Boolean = a.xor(b)
  def nor(a: Boolean, b: Boolean): Boolean = a.nor(b)
  def nand(a: Boolean, b: Boolean): Boolean = a.nand(b)
  def impl(a: Boolean, b: Boolean): Boolean = a.impl(b)

  def table2(title: String, f: (Boolean, Boolean) => Boolean): Unit = {
    println(s"\n$title")
    println("A     B     Result")
    for (a <- List(true, false); b <- List(true, false)) {
      printf("%-5s %-5s %-5s\n", a, b, f(a, b))
    }
  }
}
@main
def main(): Unit = {
  import S99Logic._

  println("Truth table for NOT:")
  println("A     Result")
  printf("%-5s %-5s\n", true, not(true))
  printf("%-5s %-5s\n", false, not(false))

  S99Logic.table2("Truth table for AND", and)
  S99Logic.table2("Truth table for OR", or)
  S99Logic.table2("Truth table for XOR", xor)
  S99Logic.table2("Truth table for IMPL", impl)
  S99Logic.table2("Truth table for EQU", equ)
  S99Logic.table2("Truth table for NOR", nor)
  S99Logic.table2("Truth table for NAND", nand)
}
