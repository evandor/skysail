package demoproduct

// ./gatling.sh -sf /Users/carsten/git/skysail/skysail.product.demo.e2e.gatling/src/gatling/scala -s demoproduct.LocalSimulation3
// ./gatling.bat -sf C:\\git\\skysail\\skysail.server.app.ref.singleentity.e2e\\src -s io.skysail.server.app.ref.singleentity.e2e.LocalSimulation3

import scala.concurrent.duration._


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class LocalSimulation3 extends Simulation {
  
  var errorSet = scala.collection.immutable.HashSet("")
  
  val baseUrl = System.getProperty("baseUrl", "http://localhost:2018")

  val httpConf = http
    .baseURL(baseUrl)
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en,de;q=0.7,en-US;q=0.3")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:48.0) Gecko/20100101 Firefox/48.0")
    //.extraInfoExtractor {
		//		extraInfo => List(extraInfo.response.body.string)
		//	}

  val uri2 = "https://code.jquery.com/ui/1.11.4"
  val uri3 = "http://cdnjs.cloudflare.com/ajax/libs/cookieconsent2/1.0.9/cookieconsent.min.js"

  //val scn = scenario("LocalSimulation").exec(Login.login, Logout.logout)

  //setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))

  // Let's have multiple populations
  val users = scenario("Users").exec(
      ShiroLogin.login, 
      RefSEA.refSEA,
      Webconsole.get,
     // Accounts.accounts,
     // RefSEA.refSEA,
     // Accounts.accounts,
      Logout.logout) // regular users can't edit
  //val admins = scenario("Admins").exec(Search.search, Browse.browse, Edit.edit)

  // Let's have 10 regular users and 2 admins, and ramp them on 10 sec so we don't hammer the server
  setUp(
    users.inject(rampUsers(25) over (25 seconds)) //,
    //admins.inject(rampUsers(2) over (10 seconds))
    ).protocols(httpConf)

}
