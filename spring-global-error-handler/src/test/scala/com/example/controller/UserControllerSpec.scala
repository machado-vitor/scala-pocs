package com.example.controller

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class UserControllerSpec extends AnyFunSuite with Matchers:
  test("sanity: 2 + 2 == 4") {
    (2 + 2) shouldBe 4
  }
