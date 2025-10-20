package com.example.config

import org.springframework.context.annotation.{Bean, Configuration}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.Module

@Configuration
class JacksonConfig:
  @Bean
  def scalaModule(): Module = DefaultScalaModule
