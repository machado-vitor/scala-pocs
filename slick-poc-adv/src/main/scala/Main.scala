import com.typesafe.scalalogging.Logger
import model.{Address, User, UserStatus}
import service.UserService
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*

object Main extends App {
  val logger = Logger("MainLogger")
  logger.info("Main started!")

  // 1) Create DB from config
  val db = Database.forConfig("slick.db")

  // 2) Instantiate only the service, which creates its DAOs internally
  val userService = new UserService(db)

  // 3) Create schema, insert some data
  Await.result(userService.createSchemaAll(), 10.seconds)
  logger.info("Schema created.")

  val addrId1 = Await.result(userService.insertAddress(Address(0, "New York", "USA")), 10.seconds)
  logger.info(s"Inserted Address ID: $addrId1")

  val addrId2 = Await.result(userService.insertAddress(Address(0, "London", "UK")), 10.seconds)
  logger.info(s"Inserted Address ID: $addrId2")

  val user1 = Await.result(userService.insertUser(User(0, "Alice", UserStatus.Active, addrId1)), 10.seconds)
  logger.info(s"Inserted User ID: $user1")

  val user2 = Await.result(userService.insertUser(User(0, "Bob", UserStatus.Inactive, addrId2)), 10.seconds)
  logger.info(s"Inserted User ID: $user2")

  val user3 = Await.result(userService.insertUser(User(0, "Charlie", UserStatus.Banned, addrId1)), 10.seconds)
  logger.info(s"Inserted User ID: $user3")

  // 4) Fetch users with addresses
  val usersWithAddr = Await.result(userService.fetchAllJoined(), 10.seconds)
  logger.info("Users with Addresses:")
  usersWithAddr.foreach(user => logger.info(user.toString))

  logger.info("Done! Closing DB connection.")
  db.close()
  sys.exit(0)
}
