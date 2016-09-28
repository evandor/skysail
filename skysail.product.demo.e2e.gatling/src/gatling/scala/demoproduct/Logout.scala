package demoproduct

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Logout {

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val logout =
      exec(http("logout")
        .get("/_logout?targetUri=/")
        .headers(headers_0))
        .pause(3)
}