package com.example.api

import com.fasterxml.jackson.annotation.JsonInclude

final case class UserDto(
  id: String,
  name: String,
  email: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
final case class ErrorResponse(
  timestamp: String,
  status: Int,
  error: String,
  message: String,
  path: String
)

