package example

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {

  class UsersTable(tag: Tag) extends Table[(Int, String, String)](tag, "users") {
    def id    = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name  = column[String]("name")
    def email = column[String]("email")
    def * = (id, name, email)
  }

  val db = Database.forConfig("slick.db") // uses src/main/resources/application.conf
  val users = TableQuery[UsersTable] // creates a query interface for the users table

  def runBlocking[T](action: DBIO[T]): T =
    Await.result(db.run(action), 10.seconds)

  println("Creating schema if needed...")
  runBlocking(users.schema.createIfNotExists)
  println("Schema created.\n")

  println("Inserting sample users...")
  val insertAction = DBIO.seq( // DBIO.seq takes a variable number of DBIO actions and combines them into a single DBIO action
    users += (0, "Alice", "alice@example.com"), // += operator is used to insert a row into the table
    users += (0, "Bob", "bob@example.com"),
    users += (0, "Charlie", "charlie@example.com")
  )
  runBlocking(insertAction)
  println("Insert complete.\n")

  println("Fetching all users...")
  val allUsers = runBlocking(users.result)
  allUsers.foreach(println)
  println()

  println("Updating Charlie's email...")
  val rowsUpdated = runBlocking {
    users.filter(_.name === "Charlie") // filter the users table to find the row with name "Charlie"
      .map(_.email)
      .update("charlie.new@example.com") // update the email field of the row
  }
  println(s"Rows updated: $rowsUpdated\n")

  println("All users after update:")
  runBlocking(users.result).foreach(println)
  println()

  println("Deleting Bob...")
  val rowsDeleted = runBlocking {
    users.filter(_.name === "Bob").delete // delete the row with name "Bob"
  }
  println(s"Rows deleted: $rowsDeleted\n")

  println("All users after delete:")
  runBlocking(users.result).foreach(println)
  println()

  db.close()
  println("Done! Database connection closed.")
}
