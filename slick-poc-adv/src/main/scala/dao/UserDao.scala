package dao

import model.{Address, User, UserStatus, UserWithAddress}
import schema.SchemaDefinition
import slick.basic.DatabasePublisher
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.{ExecutionContext, Future}

/** DAO for users table */
class UserDao(val db: Database)(implicit ec: ExecutionContext) extends SchemaDefinition {
  
  def insertUser(user: User): Future[Int] = {
    // returns auto-generated user id
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
      (u, a) <- users join addresses on (_.addressId === _.id)
    } yield (u.id, u.name, u.status, a.id, a.city, a.country)

    db.run(joinQuery.result).map { rows =>
      rows.map {
        case (uId, nm, st, addrId, city, country) =>
          UserWithAddress(uId, nm, st, Address(addrId, city, country))
      }
    }
  }

  /** Streaming example: large 'users' table. */
  def streamAllUsers(): DatabasePublisher[User] = {
    val action = users.result.withStatementParameters(fetchSize = 50)
    db.stream(action)
  }
}
