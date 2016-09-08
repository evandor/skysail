package io.skysail.server.app.ref.singleentity.e2e

// ./gatling.sh -sf /Users/carsten/git/skysail/skysail.server.app.ref.singleentity.e2e/src/ -s io.skysail.server.app.ref.singleentity.e2e.LocalSimulation2

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class LocalSimulation2 extends Simulation {

  object Login {

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
        .pause(9)
        .exec(http("post new account")
          .post("/refSEA/v1/accounts/")
          .headers(headers_0)
          .formParam("name", "account10")
          .formParam("iban", "DE00000000000000000000")
          .formParam("submit", "submit"))
        .pause(6)
        .exec(http("request_6")
          .get("/refSEA/v1/accounts/133:4/")
          .headers(headers_0))
        .pause(9)
        .exec(http("request_7")
          .post("/refSEA/v1/accounts/133:4/?method=PUT")
          .headers(headers_0)
          .formParam("name", "account10!")
          .formParam("iban", "DE00000000000000000001")
          .formParam("submitButton", ""))
        .pause(6)
        .exec(http("request_8")
          .get("/refSEA/v1/accounts/133:4/")
          .headers(headers_0))
        .pause(2)
        .exec(http("request_9")
          .post("/refSEA/v1/accounts/133:4?method=DELETE")
          .headers(headers_0))
        .pause(5)
        .exec(http("request_10")
          .get("/_logout?targetUri=/")
          .headers(headers_0))
  }

  object Logout {

    val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

    val logout =
      exec(http("logout")
        .get("/_logout?targetUri=/")
        .headers(headers_0))
        .pause(3)
  }

  val httpConf = http
    .baseURL("http://localhost:2018")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en,de;q=0.7,en-US;q=0.3")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:48.0) Gecko/20100101 Firefox/48.0")

  val uri2 = "https://code.jquery.com/ui/1.11.4"
  val uri3 = "http://cdnjs.cloudflare.com/ajax/libs/cookieconsent2/1.0.9/cookieconsent.min.js"

  //val scn = scenario("LocalSimulation").exec(Login.login, Logout.logout)

  //setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))

  // Let's have multiple populations
  val users = scenario("Users").exec(Login.login) // regular users can't edit
  //val admins = scenario("Admins").exec(Search.search, Browse.browse, Edit.edit)

  // Let's have 10 regular users and 2 admins, and ramp them on 10 sec so we don't hammer the server
  setUp(
    users.inject(rampUsers(1) over (1 seconds)) //,
    //admins.inject(rampUsers(2) over (10 seconds))
    ).protocols(httpConf)

}
