import cats.~>
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.free.Free
import cats.free.Free.liftF

import scala.annotation.tailrec
import scala.util.control.TailCalls.*


object ConsoleInterpreter extends (ConsoleOp ~> IO) {
  def apply[A](fa: ConsoleOp[A]): IO[A] = fa match {
    case PrintLine(msg) => IO(println(msg))
    case ReadLine() => IO(scala.io.StdIn.readLine())
  }
}

@main
def main(): Unit =
//  println(isEven(9999).result)
//  println(factorial(5))
  val io = program.foldMap(ConsoleInterpreter)
  io.unsafeRunSync()

// For Simple Tail-Recursive Functions → Use @tailrec
def factorial(n: Int): Int = {
  @tailrec
  def helper(acc: Int, n: Int): Int = {
    if (n <= 1) acc
    else helper(acc * n, n - 1)  // Tail recursion
  }
  helper(1, n)
}


// For Mutual Recursion or Deep Recursion → Use scala.util.control.TailCalls
def isEven(n: Int): TailRec[Boolean] = {
  if (n == 0) done(true)
  else tailcall(isOdd(n - 1))  // Calls isOdd
}

def isOdd(n: Int): TailRec[Boolean] = {
  if (n == 0) done(false)
  else tailcall(isEven(n - 1))  // Calls isEven
}

// For Functional Programming in Production → Consider Free Monads or Streaming Solutions (FS2, ZIO)

sealed trait ConsoleOp[A]
case class PrintLine(msg: String) extends ConsoleOp[Unit]
case class ReadLine() extends ConsoleOp[String]

type ConsoleIO[A] = Free[ConsoleOp, A]

def printLine(msg: String): ConsoleIO[Unit] =
  liftF(PrintLine(msg))

def readLine(): ConsoleIO[String] =
  liftF(ReadLine())

def program: ConsoleIO[Unit] = for {
  _ <- printLine("Enter your name:")
  name <- readLine()
  _ <- printLine(s"Hello, $name!")
} yield ()