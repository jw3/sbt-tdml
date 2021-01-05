lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.12",
    libraryDependencies ++= Seq(
      "org.apache.daffodil" %% "sbt-tdml-interface" % "0.1-SNAPSHOT" % Test
    ),
  )

testFrameworks += new TestFramework("org.apache.daffodil.tdml.TdmlFramework")
