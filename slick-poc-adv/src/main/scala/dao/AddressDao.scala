package dao

import model.Address
import schema.SchemaDefinition
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.{ExecutionContext, Future}

/** DAO for addresses table */
class AddressDao(db: Database)(implicit ec: ExecutionContext) extends SchemaDefinition {

  def insertAddress(addr: Address): Future[Int] = {
    db.run(addresses returning addresses.map(_.id) += addr)
  }

  def findAll(): Future[Seq[Address]] =
    db.run(addresses.result)
}
