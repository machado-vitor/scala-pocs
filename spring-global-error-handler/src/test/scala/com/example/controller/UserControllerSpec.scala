package com.example.controller

import com.example.error.GlobalExceptionHandler
import com.example.service.UserService
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.web.servlet.{MockMvc, ResultActions}
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

class UserControllerSpec extends AnyFunSuite with Matchers:

  private def buildMvc(): MockMvc =
    val service = new UserService
    val controller = new UserController(service)

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val scalaJsonConverter = new MappingJackson2HttpMessageConverter(mapper)

    MockMvcBuilders
      .standaloneSetup(controller)
      .setControllerAdvice(new GlobalExceptionHandler)
      .setMessageConverters(scalaJsonConverter)
      .build()

  test("GET /api/user/all returns empty list initially") {
    val mvc = buildMvc()

    mvc.perform(get("/api/user/all"))
      .andExpect(status().isOk)
      .andExpect(content().json("[]"))
  }

  test("POST /api/user inserts and returns it, then GET by id returns it") {
    val mvc = buildMvc()

    val body =
      """{
         |  "id": "1",
         |  "name": "Jane",
         |  "email": "jane@example.com"
         |}
         |""".stripMargin

    // Insert
    mvc.perform(
      post("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.name").value("Jane"))
      .andExpect(jsonPath("$.email").value("jane@example.com"))

    // Fetch all should contain the user (size 1)
    mvc.perform(get("/api/user/all"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$[0].id").value("1"))

    // Fetch by id
    mvc.perform(get("/api/user/id/1"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.id").value("1"))
  }

  test("GET /api/user/id/{id} returns 404 with structured error when not found") {
    val mvc = buildMvc()

    mvc.perform(get("/api/user/id/1"))
      .andExpect(status().isNotFound)
      .andExpect(jsonPath("$.error").value("Not Found"))
      .andExpect(jsonPath("$.message").value("User not found with id: 1"))
      .andExpect(jsonPath("$.path").value("/api/user/id/1"))
  }

  test("POST /api/user duplicate id returns 409 with structured error") {
    val mvc = buildMvc()

    val body =
      """{
         |  "id": 3,
         |  "name": "Jane",
         |  "email": "jane@example.com"
         |}
         |""".stripMargin

    // First insert OK
    mvc.perform(
      post("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
    ).andExpect(status().isOk)

    // Duplicate insert should fail with 409
    mvc.perform(
      post("/api/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
    )
      .andExpect(status().isConflict)
      .andExpect(jsonPath("$.error").value("Conflict"))
      .andExpect(jsonPath("$.message").value("User already exists with id: 3"))
      .andExpect(jsonPath("$.path").value("/api/user"))
  }
