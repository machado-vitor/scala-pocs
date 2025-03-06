package model

import demo.Tables.AddressesRow

case class UserWithAddress(
  id: Int,
  name: String,
  status: UserStatus,
  addresses: AddressesRow
)
