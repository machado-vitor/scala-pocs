package model

sealed trait UserStatus
object UserStatus {
  case object Active   extends UserStatus
  case object Inactive extends UserStatus
  case object Banned   extends UserStatus

  def fromString(s: String): UserStatus = s match {
    case "Active"   => Active
    case "Inactive" => Inactive
    case "Banned"   => Banned
    case other      => throw new IllegalArgumentException(s"Invalid status: $other")
  }

  def asString(u: UserStatus): String = u match {
    case Active   => "Active"
    case Inactive => "Inactive"
    case Banned   => "Banned"
  }
}
