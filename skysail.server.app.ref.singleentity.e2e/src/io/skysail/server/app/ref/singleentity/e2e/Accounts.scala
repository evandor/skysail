package io.skysail.server.app.ref.singleentity.e2e

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Accounts {

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val accounts =
        exec(http("get accounts")
          .get("/refSEA/v1/accounts/")
          .headers(headers_0))
        .pause(3)
}