name := "http-streaming"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  Seq(
    // akka
    "com.typesafe.akka" %% "akka-stream" % "2.5.2",
    "com.typesafe.akka" %% "akka-http" % "10.0.6",

    // mongo
    "org.mongodb" % "mongodb-driver-reactivestreams" % "1.4.0"
  )
}