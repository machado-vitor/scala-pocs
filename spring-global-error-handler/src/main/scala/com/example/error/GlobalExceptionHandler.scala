package com.example.error

import com.example.api.ErrorResponse
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{ExceptionHandler, RestControllerAdvice}
import jakarta.servlet.http.HttpServletRequest
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler:

  private def buildErrorResponse(
      status: HttpStatus,
      message: String,
      request: HttpServletRequest
  ): ResponseEntity[ErrorResponse] =
    val body = ErrorResponse(
      timestamp = OffsetDateTime.now().toString,
      status = status.value(),
      error = status.getReasonPhrase,
      message = message,
      path = Option(request).map(_.getRequestURI).getOrElse("")
    )
    ResponseEntity.status(status).body(body)

  @ExceptionHandler(Array(classOf[UserNotFoundException]))
  def handleUserNotFound(ex: UserNotFoundException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage, request)

  @ExceptionHandler(Array(classOf[UserAlreadyExistsException]))
  def handleUserAlreadyExists(ex: UserAlreadyExistsException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage, request)

  @ExceptionHandler(Array(classOf[IllegalArgumentException]))
  def handleBadRequest(ex: IllegalArgumentException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage, request)

  @ExceptionHandler(Array(classOf[RuntimeException]))
  def handleRuntime(ex: RuntimeException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, Option(ex.getMessage).getOrElse("Unexpected error"), request)

