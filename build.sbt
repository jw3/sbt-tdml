lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    organization := "org.apache.daffodil",
    name := "sbt-tdml",
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.12.12",
    scriptedBufferLog := false,
    scalacOptions := Seq("-unchecked",
                         "-deprecation",
                         "-feature",
                         "-encoding",
                         "utf8"),
    libraryDependencies += "org.apache.daffodil" %% "daffodil-tdml-lib" % "3.0.0"
  )
