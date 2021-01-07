package org.apache.daffodil.tdml

import sbt.Keys._
import sbt.io.IO
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Configuration, Def, File, Logger, Setting, TaskKey, Test, inConfig, taskKey}

import java.nio.file.Paths
import scala.reflect.io.Directory

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

  private def createListAllTask(resourceDir: File, tdmlExt: String, log: Logger): Seq[File] = {
    Directory(resourceDir).deepList().filter(_.isFile).filter(_.name.endsWith(tdmlExt)).map(f =>
      new File(f.path.stripPrefix(resourceDir.getPath).stripPrefix("/"))
    ).toSeq
  }

  private def createGenAllTask(targetDir: File, tdmls: Seq[File], tdmlExt: String, log: Logger): Seq[File] = {
    tdmls.map{f =>
      val suiteName = "Tdml_" + f.getName.stripSuffix(s".$tdmlExt").map(cleanFilename)
      log.debug(s"Generating TDML Suite: $suiteName")

      val file: java.io.File = Paths.get(targetDir.getAbsolutePath, s"$suiteName.scala").toFile
      IO.write(file, s"""class $suiteName extends org.apache.daffodil.tdml.TdmlSuite {
                        |  val path = "${f.getPath}"
                        |}""".stripMargin)
      file
    }
  }

  // todo;; find something to validate compilation unit names and replace this
  private def cleanFilename(c: Char) = c match {
    case '-' => '_'
    case v => v
  }
}

