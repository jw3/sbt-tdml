package org.apache.daffodil.tdml

import sbt.Keys._
import sbt.io.IO
import sbt.librarymanagement.Resolver
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Configuration, Def, File, Logger, Resolvers, Setting, TaskKey, Test, inConfig, singleFileFinder, taskKey}

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
      streams.value.log
    ),
    sourceGenerators += tdmlGenAll
  )

  private def createListAllTask(resourceDir: File, tdmlExt: String, log: Logger): Seq[File] =
    resourceDir.listFiles(_.getName.endsWith(tdmlExt))

  private def createGenAllTask(targetDir: File, tdmls: Seq[File], log: Logger): Seq[File] = {
    tdmls.foreach(f => log.out(s"TDML: ${f.getName}"))
    val tdmlsString = tdmls.map(p => s""""${p.getPath}"""").mkString(",")
    val file: java.io.File = Paths.get(targetDir.getAbsolutePath, "GenSuite.scala").toFile
    IO.write(file, s"""class GenSuite extends org.apache.daffodil.tdml.TestSuiteToo {
                     |  def tdmls = Seq($tdmlsString)
                     |}""".stripMargin)

    println(s"======= generated: ${file.getPath}")
    Seq(file)
  }
}

