package model

case class UserWithAddress(
  id: Int,
  name: String,
  status: UserStatus,
  address: Address
)
