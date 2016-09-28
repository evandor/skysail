package io.skysail.server.app.ref.singleentity.e2e

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object ShiroLogin {

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val login =
      exec(flushCookieJar)
        .exec(http("demo.local.bndrun.root")
          .get("/")
          .check(status.is(500))
          .check(substring("Applications <span class=\"caret\"></span>").find.notExists)
          .check(responseTimeInMillis.lessThan(1000))
          .headers(headers_0))
        .pause(2)
        .exec(http("_login")
          .get("/_login")
          .check(status.is(200))
          .check(responseTimeInMillis.lessThan(1000))
          .headers(headers_0))
        .pause(2)
        .exec(http("shiro _login")
          .post("/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login")
          .check(status.is(200))
          .check(responseTimeInMillis.lessThan(2000))
          //.check(substring("Applications <span class=\"caret\"></span>"))
          .headers(headers_0)
          .formParam("username", "admin")
          .formParam("password", "skysail")
          .formParam("submit", "submit"))
        .pause(1)

}