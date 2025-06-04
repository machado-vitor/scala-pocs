private trait Bakery {
  def cookBrownie(): Food
  def brewCupOfCoffee(flavor: String): Drink
}

object Bakery extends Bakery {
  override def cookBrownie(): Food = {
    Food("Brownie") // this is a syntactic sugar for `Food.apply("Brownie")`
    // Food.apply("Brownie") // this internally does: `new Food("Brownie")`
  }

  override def brewCupOfCoffee(flavor: String): Drink = {
    new Drink(flavor)
  }
}

case class Food(name: String)
class Drink(val flavor: String)

// case class have a built-in apply method, so we can create instances without using the `new` keyword