package demoproduct

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object RefSEA {

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val refSEA =
      //exec(flushCookieJar)
        exec(http("get refSEA")
          .get("/refSEA/v1")
          .check(responseTimeInMillis.lessThan(1000))
          .headers(headers_0))
        .pause(2)
        .exec(http("create account")
          .post("/refSEA/v1/accounts/")
          .check(status.is(200))
          .headers(headers_0)
          .formParam("name", "account")
          .formParam("iban", "DE00000000000000000000"))
        .pause(1)
         .exec(http("get refSEA (JSON)")
          .get("/refSEA/v1?media=json")
          .check(responseTimeInMillis.lessThan(500))
          .headers(headers_0))
        .pause(2)


}