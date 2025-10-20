package com.example.error

final class UserNotFoundException(message: String) extends RuntimeException(message)
final class UserAlreadyExistsException(message: String) extends RuntimeException(message)

