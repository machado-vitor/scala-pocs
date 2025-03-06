package dao

import slick.jdbc.PostgresProfile.api._
import demo.Tables.{Addresses, AddressesRow}

/** DAO for 'addresses' -- only returns DBIO actions */
class AddressDao {

  /** DBIO: create the schema (table). */
  def createSchema: DBIO[Unit] =
    Addresses.schema.createIfNotExists

  /** DBIO: insert a new address, returning generated ID. */
  def insertAddress(addressesRow: AddressesRow): DBIO[Int] =
    Addresses returning Addresses.map(_.id) += AddressesRow(addressesRow.id, addressesRow.city, addressesRow.country)

  /** DBIO: find by city (headOption). */
  def findByCity(city: String): DBIO[Option[AddressesRow]] =
    Addresses.filter(_.city === city).result.headOption

  /** DBIO: delete all addresses. */
  def deleteAllAddresses(): DBIO[Int] =
    Addresses.delete
}
