package service

import dao.{AddressDao, UserDao}
import model.{Address, User, UserStatus, UserWithAddress}
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.{ExecutionContext, Future}

/** Example service orchestrating user & address logic. */
class UserService(userDao: UserDao, addressDao: AddressDao)(implicit ec: ExecutionContext) {

  def createSchemaAll(): Future[Unit] = {
    val combined = (userDao.users.schema ++ addressDao.addresses.schema).createIfNotExists
    userDao.db.run(combined.transactionally).map(_ => ())
  }

  def createUserWithAddress(
    userName: String,
    status: UserStatus,
    city: String,
    country: String
  ): Future[(Int, Int)] = {

    val txn = for {
      addrId <- addressDao.addresses returning addressDao.addresses.map(_.id) +=
        Address(0, city, country)
      // Insert user referencing that new address
      userId <- userDao.users returning userDao.users.map(_.id) +=
        User(0, userName, status, addrId)
    } yield (addrId, userId)

    userDao.db.run(txn.transactionally)
  }

  def listUsersWithAddresses(): Future[Seq[UserWithAddress]] =
    userDao.fetchAllJoined()

  def updateUserStatus(userId: Int, newStatus: UserStatus): Future[Int] =
    userDao.updateStatus(userId, newStatus)
}
