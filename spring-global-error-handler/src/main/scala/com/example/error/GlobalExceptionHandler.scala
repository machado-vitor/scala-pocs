package com.example.error

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{ExceptionHandler, RestControllerAdvice}
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.converter.HttpMessageNotReadableException

import java.time.Instant

// @RestControllerAdvice is a global exception handler for Spring REST APIs.
// It intercepts exceptions thrown by any @RestController
// It combines @ControllerAdvice + @ResponseBody
@RestControllerAdvice
class GlobalExceptionHandler:
  @ExceptionHandler(Array(classOf[UserNotFoundException]))
  def handleUserNotFound(ex: UserNotFoundException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage, request)

  @ExceptionHandler(Array(classOf[UserAlreadyExistsException]))
  def handleUserAlreadyExists(ex: UserAlreadyExistsException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage, request)

  @ExceptionHandler(Array(classOf[IllegalArgumentException]))
  def handleBadRequest(ex: IllegalArgumentException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage, request)
    
  @ExceptionHandler(Array(classOf[HttpMessageNotReadableException]))
  def handleMalformedJson(ex: HttpMessageNotReadableException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    val message = Option(ex.getCause).map(_.getMessage).getOrElse("Invalid JSON format")
    buildErrorResponse(HttpStatus.BAD_REQUEST, message, request)

  @ExceptionHandler(Array(classOf[RuntimeException]))
  def handleRuntime(ex: RuntimeException, request: HttpServletRequest): ResponseEntity[ErrorResponse] =
    buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred", request)

private[error] def buildErrorResponse(
  status: HttpStatus,
  message: String,
  request: HttpServletRequest
): ResponseEntity[ErrorResponse] =
  ResponseEntity
    .status(status)
    .body(ErrorResponse(
      timestamp = Instant.now().toString,
      error = status.getReasonPhrase,
      message = message,
      path = request.getRequestURI
    ))

@JsonInclude(JsonInclude.Include.NON_NULL)
private[error] case class ErrorResponse(
  timestamp: String,
  error: String,
  message: String,
  path: String
)
