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

    loggers.foreach(_.info(s"TDML Suite: $cname"))
    s.parserTestNames().map(p => s"\t- $p").foreach(m => loggers.foreach(_.info(m)))

    val path = Paths.get(s.path())
    loggers.foreach(_.debug(s"creating Runner(${path.getParent.toString}, ${path.getFileName.toString})"))
    val runner = Runner(path.getParent.toString, path.getFileName.toString)

    s.parserTestNames().foreach { tname =>
      loggers.foreach(_.debug(s"Running parser test: $tname"))

      try {
        runner.runOneTest(tname)
        events.handle(ResultEvent.success(cname))
      } catch {
        case t: Throwable =>
          loggers.foreach(_.debug(t.getMessage))
          events.handle(ResultEvent.failure(cname, t))
      }
      runner.reset
    }
  }
}
