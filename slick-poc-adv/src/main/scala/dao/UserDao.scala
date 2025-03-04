package dao

import model.{Address, User, UserStatus, UserWithAddress}
import slick.jdbc.PostgresProfile.api.*
import demo.Tables.{Addresses, AddressesRow, Users, UsersRow}
import slick.basic.DatabasePublisher

import scala.concurrent.ExecutionContext.Implicits.global
/** DAO for 'users' -- only returns DBIO actions. */
class UserDao {

  /** DBIO: create the 'users' table (if not exists). */
  def createSchema: DBIO[Unit] =
    Users.schema.createIfNotExists

  /** DBIO: insert user, returning new ID. */
  def insertUser(user: User): DBIO[Int] =
    (Users returning Users.map(_.id)) += UsersRow(user.id, user.name, user.userStatus.toString, user.addressId)

  /** DBIO: update user status. */
  def updateStatus(userId: Int, newStatus: UserStatus): DBIO[Int] =
    Users.filter(_.id === userId).map(_.status).update(newStatus.toString)

  /** DBIO: join user+address to produce (UserWithAddress). */
  def fetchAllJoined(): DBIO[Seq[UserWithAddress]] = {
    val joinQuery = for {
      (u, a) <- Users join Addresses on (_.addressId === _.id)
    } yield (u, a)

    joinQuery.result.map { rows =>
      rows.map { case (userRow, addressesRow) =>
        val user = User(userRow.id, userRow.name, UserStatus.fromString(userRow.status), userRow.addressId)
        val addr = Address(addressesRow.id, addressesRow.city, addressesRow.country)
        UserWithAddress(user.id, user.name, user.userStatus, addr)
      }
    }
  }

  /** DBIO: wipe data from 'users' table. */
  def deleteAllUsers(): DBIO[Int] =
    Users.delete
}
