package io.skysail.server.app.ref.singleentity.e2e

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object ShiroLogin {

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val login =
      exec(flushCookieJar)
        .exec(http("root")
          .get("/")
          .headers(headers_0))
        .pause(3)
        .exec(http("_login")
          .get("/_login")
          .headers(headers_0))
        .pause(3)
        .exec(http("shiro _login")
          .post("/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login")
          .headers(headers_0)
          .formParam("username", "admin")
          .formParam("password", "skysail")
          .formParam("submit", "submit"))
        .pause(3)
        .exec(http("get refSEA")
          .get("/refSEA/v1")
          .headers(headers_0))
        .pause(3)
        .exec(http("get accounts")
          .get("/refSEA/v1/accounts/")
          .headers(headers_0))
        .pause(3)

}