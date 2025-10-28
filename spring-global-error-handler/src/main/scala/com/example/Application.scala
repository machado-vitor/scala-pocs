package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
// the @SpringBootApplication is a meta-annotation that combines the:
// @Configuration: marks the class as a source of bean definitions
// @EnableAutoConfiguration: Tells Spring to automatically configure beans based on classpath dependencies.
// @ComponentScan: Enables scanning for Spring components in the current package and sub-packages.

// Spring will Scan for @Component, @Service, @Repository, @Controller classes
// Auto-configures DataSource, JPA, Web MVC based on classpath jars.
// Creates an ApplicationContext will all discovered and configured beans.
class Application
// The empty Application class serves as a configuration holder and entry point marker.
// Spring needs a class reference to bootstrap the ApplicationContext
// The class doesn't need anything because the configuration comes from the annotation.
// Spring uses reflection to inspect this class and its annotations
// It acts as the root of the component scan by default -- scan com.example and sub-packages.

object Application:
  // this is the JVM entry point.
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[Application], args*)
    // SpringApplication is a static method that bootstraps Spring Boot.
    // Creates ApplicationContext, starts embedded Tomcat/Jetty server, register shutdown hooks.

    //classOf[Application] is scala equivalent of Application.class in Java.
      // It passes the class reference to Spring for reflection.
    // args* expands the Array[String] to individual arguments.
  }

// Startup Sequence:
  // --JVM invokes Application.main()
  // --SpringApplication.run() is called
  // --Spring Boot initialization: Creates SpringApplication instance, prepares Environment (properties, profiles)
  //   -- creates and refreshes ApplicationContext
  // --Auto-configuration phase:
  //   -- Loads META-INF/spring.factories from dependencies.
  //   -- Conditionally configures beans based on @Conditional annotations
  //   -- sets up web server, database connections, etc...
  // -- Component scanning, scans com.example package recursively, registers all annotated components as beans.
  // -- Server startup:
  //    -- Starts embedded server, deploys application, print startup logs.