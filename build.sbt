name := """E-Tailing"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc42",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  ws
)

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
