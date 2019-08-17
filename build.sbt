name := "learning-akka"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(

    //akka
    "com.typesafe.akka" %% "akka-actor" % "2.5.12",

    //akka-http
    "com.typesafe.akka" %% "akka-http"   % "10.1.1",

    "com.typesafe.akka" %% "akka-stream" % "2.5.11",

    "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
    // akka-http-cache
    "com.typesafe.akka" %% "akka-http-caching" % "10.1.0",
    //scalatest
    "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)
