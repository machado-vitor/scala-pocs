package model

case class User(
  id: Int,
  name: String,
  userStatus: UserStatus,
  addressId: Int,
)

