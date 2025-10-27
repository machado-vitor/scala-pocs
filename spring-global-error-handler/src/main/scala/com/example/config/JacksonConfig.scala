package com.example.config

import org.springframework.context.annotation.{Bean, Configuration}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.Module

@Configuration
class JacksonConfig:
  @Bean // SB auto-configuration automatically detects any Module beans and register them with the ObjectMapper
  def scalaModule(): Module = DefaultScalaModule
  // the scalaModule method configures Jackson to work with Scala types by 
  // registering the DefaultScalaModule.


// DefaultScalaModule: This jackson module adds serialization and deserialization support for Scala-specific features:
  // - Option types (converts Some(value) to value, None to null)
  // - Case classes: maps json fields to constructor parameters - Handles the apply and copy methods.
  
// Integration flow:
  
//  Spring Boot startup
//  ↓
//  Scans for @Configuration classes
//  ↓
//  Finds JacksonConfig
//  ↓
//  Executes @Bean methods
//  ↓
//  Registers DefaultScalaModule with ObjectMapper
//  ↓
//  ObjectMapper now understands Scala types