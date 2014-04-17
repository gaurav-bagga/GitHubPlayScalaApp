name := "GitHubApp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.0.1",
  cache,
  jdbc,
  "com.h2database" % "h2" % "1.3.175",
  "org.webjars"   %% "webjars-play"  % "2.2.0",
  "org.webjars" % "bootstrap" % "3.0.0"
)     

play.Project.playScalaSettings
