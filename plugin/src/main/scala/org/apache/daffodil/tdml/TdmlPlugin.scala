package org.apache.daffodil.tdml

import sbt.Keys._
import sbt.io.IO
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Configuration, Def, File, Logger, Setting, TaskKey, Test, inConfig, taskKey}

import java.nio.file.Paths

object TdmlPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] = tdmlSettings(Test)

  def tdmlSettings(configurations: Configuration*): Seq[Setting[_]] =
    configurations.foldLeft(List.empty[Setting[_]])(_ ++ inConfig(_)(needsScoped))

  val tdmlResourceDirectory =
    taskKey[File]("TDML test directory")

  val tdmlDescriptorExt =
    taskKey[String]("TDML test descriptor file extension")

  val tdmlListAll: TaskKey[Seq[File]] =
    taskKey[Seq[File]]("List TDML test descriptors")

  val tdmlGenAll: TaskKey[Seq[File]] =
    taskKey[Seq[File]]("Generate TDML suites")

  private def needsScoped = Seq(
    tdmlResourceDirectory := resourceDirectory.value,
    tdmlDescriptorExt := "tdml",
    tdmlListAll := createListAllTask(
      tdmlResourceDirectory.value,
      tdmlDescriptorExt.value,
      streams.value.log
    ),
    tdmlGenAll := createGenAllTask(
      sourceManaged.value,
      tdmlListAll.value,
      tdmlDescriptorExt.value,
      streams.value.log
    ),
    sourceGenerators += tdmlGenAll
  )

  private def createListAllTask(resourceDir: File, tdmlExt: String, log: Logger): Seq[File] =
    resourceDir.listFiles(_.getName.endsWith(tdmlExt)).map(f =>
      Paths.get(f.getPath.stripPrefix(resourceDir.getPath)).toFile
    )

  private def createGenAllTask(targetDir: File, tdmls: Seq[File], tdmlExt: String, log: Logger): Seq[File] = {
    tdmls.map{f =>
      val suiteName = f.getName.stripSuffix(s".$tdmlExt")
      log.out(s"Generating TDML Suite: $suiteName")

      val file: java.io.File = Paths.get(targetDir.getAbsolutePath, s"$suiteName.scala").toFile
      IO.write(file, s"""class $suiteName extends org.apache.daffodil.tdml.TdmlSuite {
                        |  val path = "${f.getPath}"
                        |}""".stripMargin)
      file
    }
  }
}

