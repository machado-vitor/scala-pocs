// class - Blueprint for objects - can have constructor params, methods, fields
// object - singleton instance
// trait - interface with optional implementation
// case class - immutable data class with pattern matching support
//            - automatically gives `equals`, `hashCode`, `copy`, `apply`, `unapply`
// case object - singleton version of case class - useful for ADTs/enums
// enum (Scala 3)
// sealed class / trait - restrict inheritance to same file - Ensure exhaustive pattern matching
// abstract class - class with unimplemented members


// ABOUT OBJECT
// --------------------------------------------------------------------------------
// An object is a singleton,
// only one instance in the whole program
// instantiated automatically
// used for: utility methods, constants, app entry points (@main or def main), pure FP-style stateless helpers
// --------------------------------------------------------------------------------
// Utility methods: This is like a static from Java, but Scala avoids static, so they live in an object.
// --------------------------------------------------------------------------------
// Constants/configuration/shared values:
//object Config:
//  val AppName = "MyApp"
//  val Version = "1.0.0"
//  val MaxConnections = 10
// --------------------------------------------------------------------------------
// Application entry points
// JVM needs a static entry point, but Scala avoids static keyword
// So the compiler puts it into an object.
// --------------------------------------------------------------------------------
// Pure FP-style stateless helpers
// Object as a singleton module of pure functions
// no state, no mutation, no side effects
// object MathOps:
//   def add(a: Int, b: Int) = a + b
//   def mul(a: Int, b: Int) = a * b

// ABOUT COMPANION OBJECT
// --------------------------------------------------------------------------------
// Has the same name as a class
// Lives in the same file
// Can access private members of the class
// provides class-level functionality(like static methods)
// --------------------------------------------------------------------------------
// Factory methods (apply)
// class Person(val name: String, val age: Int)
// object Person:
//    def apply(name: String, age: Int) = new Person(name, age)
