package demoproduct

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Webconsole {

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val get =
        exec(http("get webconsole")
          .get("/webconsole/v1")
          .headers(headers_0))
        .pause(1)
}