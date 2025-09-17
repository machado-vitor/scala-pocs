class DefaultCash(
  private val dollars: Float
) extends Cash {
  override def multiply(multiplicand: Int): Cash =
    new DefaultCash(this.dollars * multiplicand)

  override def amount: Float = dollars
}

object DefaultCash {
  def apply(dollars: Int): DefaultCash = new DefaultCash(dollars.toFloat)
  def apply(dollars: Float): DefaultCash = new DefaultCash(dollars)
  def apply(dollars: Double): DefaultCash = new DefaultCash(dollars.toFloat)
}
