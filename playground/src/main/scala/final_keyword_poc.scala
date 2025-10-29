// final in a class
final class PaymentService {
  def process(): Unit = println("processing...")
}

// class MyPaymentService extends PaymentService
// error: illegal inheritance from final class PaymentService

// Why use it:
// Immutability - To make the class behaviour fixed and avoid misuse or unintended inheritance.
// Performance - JVM can optimize, gives the JIT (HotSpot) stronger guarantees, which unlocks several optimizations

// --------------------------------------------------------------------------------------------------

// final on a method
// subclasses cannot override it
class Account {
  final def withdraw(amount: Double): Unit =
    println(s"Withdrawing $$amount")
}

class SavingsAccount extends Account {
  // cannot override final method
//  override def withdraw(amount: Double): Unit = println("Blocked")
}

// When use it:
// Preserve core behaviour in subclasses

// --------------------------------------------------------------------------------------------------

// final on a variable (val)
// Scala already enforces immutability for `val`,
// making it final only prevents redefinition in subclasses.
class Config {
  final val version = "1.0"
}

class MyConfig extends Config {
//  override val version = "2.0" //cannot override final val version
}

// Why use it:
//To guarantee the value stays the same across all subclasses.