import sbt._
import com.github.siasia._
import WebPlugin._
import PluginKeys._
import Keys._

object Build extends sbt.Build {
  import Dependencies._
  
  lazy val myProject = Project("DebtPayDown", file("."))
    .settings(WebPlugin.webSettings: _*)
    .settings(port in config("container") := 8080)
    .settings(
      organization  := "com.webwino",
      version       := "0.1.0",
      scalaVersion  := "2.9.1",
      scalacOptions := Seq("-deprecation", "-encoding", "utf8"),
      resolvers     ++= Dependencies.resolutionRepos,
      libraryDependencies ++= Seq(
		Compile.scalatra,
		Compile.scalatraScalate,
        Compile.akkaActor,
        Compile.jodaTime,
		Test.scalatraSpecs2,
        Container.jettyWebApp,
        Container.akkaSlf4j,
        Container.slf4j,
        Container.logback,
		Provided.javaxServlet
      )
    )
}

object Dependencies {
  val resolutionRepos = Seq(
    ScalaToolsSnapshots,
    "Typesafe repo" at "http://repo.typesafe.com/typesafe/releases/",
	"Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  )

  object V {
    val akka    = "2.0.2"
	val scalatra = "2.2.0-SNAPSHOT"
	val scalatraSpecs2 = "2.1.0-SNAPSHOT"
    val jetty   = "8.1.0.v20120127"
    val slf4j   = "1.6.4"
    val logback = "1.0.0"
    val jodaTime = "2.0"
    val apacheHttp = "3.1"
	val javaxServlet = "2.5"
  }
  
  object Provided {
	val javaxServlet = "javax.servlet" % "servlet-api" % V.javaxServlet % "provided"
  }

  object Compile {
    val akkaActor   = "com.typesafe.akka"         %  "akka-actor"      % V.akka        % "compile"
	val scalatra    = "org.scalatra"			  %  "scalatra"		   % V.scalatra    % "compile"
	val scalatraScalate = "org.scalatra"		  %  "scalatra-scalate" % V.scalatra   % "compile"
    val jodaTime    = "joda-time"                 %  "joda-time"       % V.jodaTime    % "compile"
    val apacheHttp  = "commons-httpclient"        % "commons-httpclient" % V.apacheHttp % "compile"
  }

  object Test {
	val scalatraSpecs2 = "org.scalatra"			  %% "scalatra-specs2" % V.scalatraSpecs2 % "test"
  }

  object Container {
    val jettyWebApp = "org.eclipse.jetty"         %  "jetty-webapp"    % V.jetty   % "container"
    val akkaSlf4j   = "com.typesafe.akka"         %  "akka-slf4j"      % V.akka
    val slf4j       = "org.slf4j"                 %  "slf4j-api"       % V.slf4j
    val logback     = "ch.qos.logback"            %  "logback-classic" % V.logback
  }
}