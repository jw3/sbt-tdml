package org.apache.daffodil.tdml

import org.scalatools.testing
import org.scalatools.testing._

import java.nio.file.Paths

class TdmlSuiteFingerprint extends SubclassFingerprint {
  val superClassName: String = classOf[TdmlSuite].getName
  val isModule: Boolean = false
}

final class TdmlFramework extends Framework {
  def name(): String = "TDML"
  def tests(): Array[Fingerprint] = Array(new TdmlSuiteFingerprint)

  def testRunner(testClassLoader: ClassLoader, loggers: Array[Logger]): testing.Runner =
    new TdmlRunner(testClassLoader, loggers)
}

class TdmlRunner(testClassLoader: ClassLoader, loggers: Array[Logger]) extends org.scalatools.testing.Runner {
  def run(cname: String, fp: TestFingerprint, events: EventHandler, args: Array[String]): Unit = {
    val sc = testClassLoader.loadClass(cname)
    val s = sc.getDeclaredConstructor().newInstance().asInstanceOf[TdmlSuite]

    val path = Paths.get(s.path())
    val tests = s.parserTestNames()
    loggers.foreach(_.info(s"TDML Suite: ${s.path()}"))
    tests.map(p => s"\t- $p").foreach(m => loggers.foreach(_.info(m)))

    val (dir: String, file: String) = Option(path.getParent) -> Option(path.getFileName) match {
      case (Some(d), Some(f)) => d.toString -> f.toString
      case (None, Some(f)) => "" -> f.toString
      case _ => throw new RuntimeException(s"invalid path: ${path}")
    }
    loggers.foreach(_.debug(s"creating Runner($dir, $file)"))

    // the runner needs loaded from the tcl
    val rc = testClassLoader.loadClass(classOf[Runner].getName).asInstanceOf[Class[Runner]]
    val rctor = rc.getDeclaredConstructor(classOf[String], classOf[String])
    val runner = rctor.newInstance(dir, file)

    tests.foreach { tname =>
      loggers.foreach(_.debug(s"Running parser test: $tname"))

      try {
        runner.runOneTest(tname)
        events.handle(SbtTestEvent.success(cname))
      } catch {
        case t: Throwable =>
          loggers.foreach(_.debug(t.getMessage))
          events.handle(SbtTestEvent.failure(cname, t))
      }
      runner.reset
    }
  }
}

case class SbtTestEvent(result: Result, testName: String, description: String, error: Throwable) extends Event
object SbtTestEvent {
  def success(name: String): SbtTestEvent = SbtTestEvent(Result.Success, name, name, null)
  def failure(name: String, t: Throwable): SbtTestEvent = SbtTestEvent(Result.Failure, name, name, t)
}
