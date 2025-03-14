package dao

import model.{Address, User, UserStatus, UserWithAddress}
import schema.SchemaDefinition
import slick.basic.DatabasePublisher
import slick.jdbc.PostgresProfile.api.*
import slick.jdbc.{ResultSetConcurrency, ResultSetType}

import scala.concurrent.{ExecutionContext, Future}

/** DAO for users table */
class UserDao(val db: Database)(implicit ec: ExecutionContext) extends SchemaDefinition {
  
  def insertUser(user: User): Future[Int] = {
    // returns auto-generated user id -- returning users.map(_.id))
    db.run(users returning users.map(_.id) += user)
  }

  def findById(userId: Int): Future[Option[User]] =
    db.run(users.filter(_.id === userId).result.headOption)

  def updateStatus(userId: Int, newStatus: UserStatus): Future[Int] = {
    val action = users.filter(_.id === userId).map(_.status).update(newStatus)
    db.run(action)
  }

  def fetchAllJoined(): Future[Seq[UserWithAddress]] = {
    val joinQuery = for {
      (u, a) <- users join addresses on (_.addressId === _.id) // Joining users and addresses
    } yield (u.id, u.name, u.status, a.id, a.city, a.country)

    db.run(joinQuery.result).map { rows =>
      rows.map {
        case (uId, nm, st, addrId, city, country) =>
          UserWithAddress(uId, nm, st, Address(addrId, city, country)) // Mapping result to case classes
      }
    }
  }

  def wipeAllDbData(): Future[Int] = {
    val action = (users.delete andThen addresses.delete)
    db.run(action)
  }

  def findAll(): Future[Seq[User]] =
    db.run(users.result)

  /** Streaming example: large 'users' table. */
  def streamAllUsers(): DatabasePublisher[User] = {
    db.stream(users.result)
    // Instead of fetching all users at once, this method streams them lazily.
  }
}
