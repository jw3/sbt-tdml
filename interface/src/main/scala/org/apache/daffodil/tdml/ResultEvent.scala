package org.apache.daffodil.tdml

import org.apache.daffodil.tdml.tdmlext.TdmlTestResult
import org.scalatools.testing.{Event, Result}
import org.scalatools.testing.Result._

case class ResultEvent(result: Result, testName: String, description: String, error: Throwable) extends Event

object ResultEvent {
  val failure = (result: TdmlTestResult) => event(Failure, result)
  val skipped = (result: TdmlTestResult) => event(Skipped, result)
  val success = (result: TdmlTestResult) => event(Success, result)

  private[this] def event(result: Result, tdmlResult: TdmlTestResult) =
    ResultEvent(result, tdmlResult.name, tdmlResult.description, tdmlResult.error.orNull)

  def success(testName: String) = ResultEvent(Success, testName, testName, null)
  def failure(testName: String, error: Throwable) = ResultEvent(Failure, testName, testName, error)

}
