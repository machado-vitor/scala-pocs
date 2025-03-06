package schema

import model.{Address, User, UserStatus}
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.{ForeignKeyQuery, ProvenShape}

/** Contains table definitions for addresses and users. */
trait SchemaDefinition {

  // MappedColumnType for the enum-like UserStatus
  implicit val userStatusColumn: BaseColumnType[UserStatus] =
    MappedColumnType.base[UserStatus, String](
      UserStatus.asString,
      UserStatus.fromString
    )

  class AddressesTable(tag: Tag) extends Table[Address](tag, "addresses") {
    def id      = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def city    = column[String]("city")
    def country = column[String]("country")

    // Map columns to the Address case class
    def * : ProvenShape[Address] =
      (id, city, country) <> // (Shape mapping operator) maps between the tuple format and case class.
        (Address.apply.tupled, Address.unapply)
  }

  val addresses = TableQuery[AddressesTable]

  class UsersTable(tag: Tag) extends Table[User](tag, "users") {
    def id        = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name      = column[String]("name")
    def status    = column[UserStatus]("status")
    def addressId = column[Int]("address_id")

    def * : ProvenShape[User] =
      (id, name, status, addressId) <> (User.apply.tupled, User.unapply)

    // Foreign key constraint from users.address_id -> addresses.id
    def addressFk: ForeignKeyQuery[AddressesTable, Address] =
      foreignKey("FK_addresses", addressId, addresses)(_.id)
  }

  val users = TableQuery[UsersTable]
}
