package org.apache.daffodil.tdml

import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Configuration, File, Logger, Setting, TaskKey, Test, inConfig, taskKey}

object TdmlPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = JvmPlugin
  override def projectSettings = tdmlSettings(Test)

  def tdmlSettings(configurations: Configuration*): Seq[Setting[_]] =
    configurations.foldLeft(List.empty[Setting[_]])(_ ++ inConfig(_)(needsScoped))

  val tdmlResourceDirectory =
    taskKey[File]("TDML test directory")

  val tdmlDescriptors =
    taskKey[String]("TDML test descriptor file extension")

  val tdmlListAll: TaskKey[Iterable[File]] =
    taskKey[Iterable[File]]("List TDML test descriptors")

  val tdmlTestAll: TaskKey[Unit] =
    taskKey[Unit]("Run TDML tests")

  private def needsScoped = Seq(
      tdmlResourceDirectory := resourceDirectory.value,
      tdmlDescriptors := "tdml",
      tdmlListAll := createListAllTask(
        tdmlResourceDirectory.value,
        tdmlDescriptors.value,
        streams.value.log
      ),
      tdmlTestAll := createTestAllTask(
        tdmlListAll.value,
        streams.value.log
      )
    )

  private def createListAllTask(resourceDir: File, tdmlExt: String, log: Logger): Iterable[File] =
    resourceDir.listFiles(_.getName.endsWith(tdmlExt))

  private def createTestAllTask(tdmls: Iterable[File], log: Logger): Unit = {
    tdmls.foreach(f => log.out(s"TDML: ${f.getName}"))
  }
}
