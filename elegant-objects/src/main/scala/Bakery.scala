private trait Bakery {
  // These are not actually methods of an object, they are procedures.
  // This is not object-oriented.
  def cookBrownie(): Food
  def brewCupOfCoffee(flavor: String): Drink
}

object Bakery extends Bakery {
  override def cookBrownie(): Food = {
    Food("Brownie") // this is a syntactic sugar for `Food.apply("Brownie")`
    // Food.apply("Brownie") // this internally does: `new Food("Brownie")`
  }

  override def brewCupOfCoffee(flavor: String): Drink = {
    Drink(flavor)
  }
}

case class Food(name: String)
case class Drink(flavor: String)

// case class have a built-in apply method, so we can create instances without using the `new` keyword