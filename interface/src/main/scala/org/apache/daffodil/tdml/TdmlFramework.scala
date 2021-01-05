package org.apache.daffodil.tdml

import org.scalatools.testing
import org.scalatools.testing._

import java.nio.file.Paths

class TdmlSuiteFingerprint extends SubclassFingerprint {
  val superClassName: String = "org.apache.daffodil.tdml.TestSuiteToo"
  val isModule: Boolean = false
}

final class TdmlFramework extends Framework {
  def name(): String = "TDML"
  def tests(): Array[Fingerprint] = Array(new TdmlSuiteFingerprint)

  def testRunner(testClassLoader: ClassLoader, loggers: Array[Logger]): testing.Runner = {
    println("((((((((((((( creating the runner ))))))))))))))))")
    new TdmlRunner(testClassLoader, loggers)
  }
}

class TdmlRunner(testClassLoader: ClassLoader, loggers: Array[Logger]) extends org.scalatools.testing.Runner {
  def run(testClassName: String, fingerprint: TestFingerprint, eventHandler: EventHandler, args: Array[String]) = {
    loggers.foreach(_.info(s"=== TDML test: $testClassName"))

    val sc = testClassLoader.loadClass(testClassName)
    val s = sc.getDeclaredConstructor().newInstance().asInstanceOf[TestSuiteToo]
    println("=== TDML Runner:")
    s.tdmls.map(p => s"\t$p").foreach(println)

    val path = Paths.get(testClassName)
    val runner = Runner(path.getParent.toString, path.getFileName.toString)
    try {
      runner.runOneTest("booleanDefault")
      eventHandler.handle(ResultEvent.success(testClassName))
    } catch {
      case t: Throwable =>
        eventHandler.handle(ResultEvent.failure(testClassName, t))
    }
  }
}
