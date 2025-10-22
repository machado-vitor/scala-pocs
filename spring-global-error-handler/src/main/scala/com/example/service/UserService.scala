package com.example.service

import com.example.api.UserDto
import com.example.error.{UserAlreadyExistsException, UserNotFoundException}
import org.springframework.stereotype.Service

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters.*

@Service
class UserService:
  private val store = new ConcurrentHashMap[Long, UserDto]()

  def findAll(): List[UserDto] =
    store.values().asScala.toList

  def findById(id: Long): UserDto =
    Option(store.get(id)).getOrElse(throw new UserNotFoundException(s"User not found with id: $id"))

  def insert(user: UserDto): UserDto =
    val existing = store.putIfAbsent(user.id, user)
    if existing != null then throw new UserAlreadyExistsException(s"User already exists with id: ${user.id}")
    user
