// Case classes

case class Book(isbn: String)

val frankenstein = Book("978-0486282114")
// we don't need to pass the `new` keyword, case classes have an apply method by default which takes care of object construction.
// by default the parameters are immutables


// Comparison
// Instances of case classes are comparable by structure, not by reference.

case class Message(sender: String, recipient: String, body: String)

val message2 = Message("jorge@catalonia.es", "guillaume@quebec.ca", "Com va?")
val message3 = Message("jorge@catalonia.es", "guillaume@quebec.ca", "Com va?")
val messagesAreTheSame = message2 == message3  // true


// copying

// We can create a shallow copy by using copy method

val message4 = Message(
  sender = "julien@bretagne.fr",
  recipient = "travis@washington.us",
  body = "Me zo o komz gant ma amezeg"
)
val message5 = message4.copy(sender = message4.recipient, recipient = "claire@bourgogne.fr")

// message5.sender  // travis@washington.us
// message5.recipient // claire@bourgogne.fr
// message5.body  // "Me zo o komz gant ma amezeg"