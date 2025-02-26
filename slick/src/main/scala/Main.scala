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

  val db = Database.forConfig("slick.db")
  val users = TableQuery[UsersTable]

  def runBlocking[T](action: DBIO[T]): T =
    Await.result(db.run(action), 10.seconds)

  println("Creating schema if needed...")
  runBlocking(users.schema.createIfNotExists)
  println("Schema created.\n")

  println("Inserting sample users...")
  val insertAction = DBIO.seq(
    users += (0, "Alice", "alice@example.com"),
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
    users.filter(_.name === "Charlie")
      .map(_.email)
      .update("charlie.new@example.com")
  }
  println(s"Rows updated: $rowsUpdated\n")

  println("All users after update:")
  runBlocking(users.result).foreach(println)
  println()

  println("Deleting Bob...")
  val rowsDeleted = runBlocking {
    users.filter(_.name === "Bob").delete
  }
  println(s"Rows deleted: $rowsDeleted\n")

  println("All users after delete:")
  runBlocking(users.result).foreach(println)
  println()

  db.close()
  println("Done! Database connection closed.")
}
