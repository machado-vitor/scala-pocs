package service

import dao.{AddressDao, UserDao}
import model.{Address, User, UserStatus, UserWithAddress}
import slick.jdbc.PostgresProfile.api.*
import slick.basic.DatabasePublisher

import scala.concurrent.{ExecutionContext, Future}

class UserService(db: Database)(implicit ec: ExecutionContext) {

  // Create the DAOs internally, no need for them in Main
  private val addressDao = new AddressDao()
  private val userDao    = new UserDao()

  /** Create both schemas in one go (if needed). */
  def createSchemaAll(): Future[Unit] = {
    val action = for {
      _ <- addressDao.createSchema
      _ <- userDao.createSchema
    } yield ()
    db.run(action.transactionally).map(_ => ())
  }

  /**
   * Example: Create or update an Address by city, then insert a User referencing that address.
   * Returns the newly inserted user ID.
   */
  def createOrUpdateUserWithAddress(
    userName: String,
    status: UserStatus,
    city: String,
    country: String
  ): Future[Int] = {

    val txn = for {
      existingAddrOpt <- addressDao.findByCity(city)
      addrId <- existingAddrOpt match {
        case Some(addrRow) => DBIO.successful(addrRow.id)
        case None          => addressDao.insertAddress(Address(0, city, country))
      }
      userId <- userDao.insertUser(User(0, userName, status, addrId))
    } yield userId

    db.run(txn.transactionally)
  }

  /** Fetch all users joined with addresses (DBIO-based), run here. */
  def fetchAllJoined(): Future[Seq[UserWithAddress]] =
    db.run(userDao.fetchAllJoined())

  /** Wipe all data from both tables in one transaction. */
  def wipeAllUserData(): Future[Int] = {
    val txn = for {
      deletedUsers     <- userDao.deleteAllUsers()
      deletedAddresses <- addressDao.deleteAllAddresses()
    } yield deletedUsers + deletedAddresses
    db.run(txn.transactionally)
  }

  /** Insert an Address, returning the new ID. */
  def insertAddress(addr: Address): Future[Int] =
    db.run(addressDao.insertAddress(addr))

  /** Insert a User, returning the new ID. */
  def insertUser(user: User): Future[Int] =
    db.run(userDao.insertUser(user))
}
