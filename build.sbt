lazy val commonSettings = Seq(
  organization := "org.apache.daffodil",
  version := "0.2-SNAPSHOT",
  scalaVersion := "2.12.12",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding",
    "utf8"),
)


lazy val api = (project in file("api"))
  .settings(
    name := "sbt-tdml-api",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "1.3.0"
    )
  )
  .settings(commonSettings)

lazy val plugin = (project in file("plugin"))
  .dependsOn(api, interface)
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-tdml",
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
  )
  .settings(commonSettings)

lazy val interface = (project in file("interface"))
  .dependsOn(api)
  .settings(
    name := "sbt-tdml-interface",
    libraryDependencies ++= Seq(
      "org.apache.daffodil" %% "daffodil-tdml-lib" % "3.0.0",
      "org.apache.daffodil" %% "daffodil-tdml-processor" % "3.0.0",
      "org.scala-sbt" % "test-interface" % "1.0"
    )
  )
  .settings(commonSettings)

lazy val root = (project in file("."))
  .aggregate(api, plugin, interface)
  .settings(
    name := "tdml-support",
    skip in publish := true
  )

