name := "quartz-boiler"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.quartz-scheduler" % "quartz" % "2.2.1",
  "org.springframework" % "spring-context" % "4.1.6.RELEASE",
  "org.springframework" % "spring-context-support" % "4.1.6.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.6.RELEASE"
)