name := "skysail.restlet REPL playground"
version := "1.0"
scalaVersion := "2.11.8"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.1"

initialCommands in console += "import org.json4s._\n"
initialCommands in console += "import org.json4s.jackson.JsonMethods._\n"
