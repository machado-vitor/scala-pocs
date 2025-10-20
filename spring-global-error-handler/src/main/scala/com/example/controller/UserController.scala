package com.example.controller

import com.example.api.UserDto
import com.example.service.UserService
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, PostMapping, RequestBody, RequestMapping, RestController}
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping(Array("/api"))
class UserController(userService: UserService):

  @GetMapping(Array("/user/all"))
  def findAllUsers(): List[UserDto] =
    userService.findAll()

  @GetMapping(Array("/user/id/{id}"))
  def findUserById(@PathVariable id: String): ResponseEntity[UserDto] =
    val user = userService.findById(id) // throws if not found
    ResponseEntity.ok(user)

  @PostMapping(Array("/user"))
  def insertUser(@RequestBody userDto: UserDto): ResponseEntity[UserDto] =
    val inserted = userService.insert(userDto) // throws if already exists
    ResponseEntity.ok(inserted)

