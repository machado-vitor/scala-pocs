class DefaultCash(
  private val dollars: Float
) extends Cash {
  override def multiply(multiplicand: Float): Cash =
    new DefaultCash(this.dollars * multiplicand)
}
