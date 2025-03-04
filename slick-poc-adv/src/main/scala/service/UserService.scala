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

  def createOrUpdateUserWithAddress(
    userName: String,
    status: UserStatus,
    city: String,
    country: String
  ): Future[Int] = {

    val existingCityQuery = addressDao.addresses.filter(_.city === city).result.headOption

    val combinedAction = for {
      existingAddrOpt <- existingCityQuery
      addrId <- existingAddrOpt match {
        // If the address already exists, use it
        case Some(addr) => DBIO.successful(addr.id)
        // Otherwise insert a new address
        case None => addressDao.addresses returning addressDao.addresses.map(_.id) +=
          Address(0, city, country)
      }

      userId <- userDao.users returning userDao.users.map(_.id) +=
        User(0, userName, status, addrId)
    } yield userId

    // Wrap everything in a single transaction
    userDao.db.run(combinedAction.transactionally)
  }

  def listUsersWithAddresses(): Future[Seq[UserWithAddress]] =
    userDao.fetchAllJoined()

  def updateUserStatus(userId: Int, newStatus: UserStatus): Future[Int] =
    userDao.updateStatus(userId, newStatus)
    
  def wipeAllUSerData(): Unit = {
    userDao.wipeAllDbData()
  }
}
