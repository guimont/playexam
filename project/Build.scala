import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "testexam"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "com.typesafe.play" %% "play-slick" % "0.3.3",
    "com.typesafe.slick" %% "slick" % "1.0.1",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
