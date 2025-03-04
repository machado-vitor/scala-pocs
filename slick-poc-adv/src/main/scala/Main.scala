import com.typesafe.scalalogging.Logger
import dao.{AddressDao, UserDao}
import model.{Address, User, UserStatus}
import org.reactivestreams.{Subscriber, Subscription}
import service.UserService
import slick.basic.DatabasePublisher
import slick.jdbc.PostgresProfile.api.*
import scala.compiletime.uninitialized

import java.util.concurrent.CountDownLatch
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, ExecutionContext}

object Main extends App {

  // Create a logger instance
  val logger = Logger("MainLogger")
  logger.info("Main started!")

  // 1) Create DB from config
  val db = Database.forConfig("slick.db")

  // 2) Instantiate DAOs & Services
  val addressDao = new AddressDao(db)
  val userDao    = new UserDao(db)
  val userService= new UserService(userDao, addressDao)

  // 3) Bootstrapping: Create schema, insert some data
  // Ensure schema exists before inserting
  Await.result(userService.createSchemaAll(), 10.seconds)
  logger.info("Schema created.")

  val addrId1 = Await.result(addressDao.insertAddress(Address(0, "New York", "USA")), 10.seconds)
  logger.info(s"Inserted Address ID: $addrId1")
  val addrId2 = Await.result(addressDao.insertAddress(Address(0, "London", "UK")), 10.seconds)
  logger.info(s"Inserted Address ID: $addrId2")
  val user1 = Await.result(userDao.insertUser(User(0, "Alice", UserStatus.Active, addrId1)), 10.seconds)
  logger.info(s"Inserted User ID: $user1")

  val user2 = Await.result(userDao.insertUser(User(0, "Bob", UserStatus.Inactive, addrId2)), 10.seconds)
  logger.info(s"Inserted User ID: $user2")

  val user3 = Await.result(userDao.insertUser(User(0, "Charlie", UserStatus.Banned, addrId1)), 10.seconds)
  logger.info(s"Inserted User ID: $user3")

  // Fetch users
  val users = Await.result(userDao.fetchAllJoined(), 10.seconds)
  logger.info("Users with Addresses:")
  users.foreach(user => logger.info(user.toString))

  val publisher: DatabasePublisher[User] = userDao.streamAllUsers()
  val latch = new CountDownLatch(1)

  val subscriber = new Subscriber[User] {
    private var sub: Subscription = uninitialized

    override def onSubscribe(subscription: Subscription): Unit = {
      this.sub = subscription
      subscription.request(1) // request the first item
    }

    override def onNext(user: User): Unit = {
      println(s"Received user: $user")
      sub.request(1) // request the next item
    }

    override def onError(t: Throwable): Unit = {
      println(s"Stream error: ${t.getMessage}")
      latch.countDown()
    }

    override def onComplete(): Unit = {
      println("Stream completed!")
      latch.countDown()
    }
  }

  // 4. Subscribe and wait until stream finishes
  publisher.subscribe(subscriber)
  latch.await()

//  userService.wipeAllUSerData() // wipe data

  logger.info("Done! Closing DB connection.")
  db.close()
  sys.exit(0)
}
