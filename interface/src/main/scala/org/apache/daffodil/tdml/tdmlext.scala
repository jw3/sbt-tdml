package org.apache.daffodil.tdml

object tdmlext {
  sealed trait TdmlTestResult {
    def name: String
    def description: String
    def error: Option[Throwable]
  }

  case class Success(name: String, description: String) extends TdmlTestResult {
    def error: Option[Throwable] = None
  }
  case class Failure(name: String, description: String, cause: Throwable) extends TdmlTestResult {
    def error: Option[Throwable] = Some(cause)
  }
}
