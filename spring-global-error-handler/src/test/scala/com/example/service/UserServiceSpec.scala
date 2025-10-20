package com.example.service

import com.example.api.UserDto
import com.example.error.{UserAlreadyExistsException, UserNotFoundException}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class UserServiceSpec extends AnyFunSuite with Matchers:
  test("insert and fetch user") {
    val svc = new UserService
    val user = UserDto("1", "Jane", "jane@example.com")

    val inserted = svc.insert(user)
    inserted shouldBe user

    val all = svc.findAll()
    all should contain (user)

    val byId = svc.findById("1")
    byId shouldBe user
  }

  test("findById throws UserNotFoundException when missing") {
    val svc = new UserService
    val ex = intercept[UserNotFoundException] {
      svc.findById("missing")
    }
    ex.getMessage should include ("missing")
  }

  test("insert throws UserAlreadyExistsException on duplicate id") {
    val svc = new UserService
    val user = UserDto("1", "Jane", "jane@example.com")
    svc.insert(user)
    val ex = intercept[UserAlreadyExistsException] {
      svc.insert(user)
    }
    ex.getMessage should include ("1")
  }

