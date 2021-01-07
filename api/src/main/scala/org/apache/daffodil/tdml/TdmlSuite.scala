package org.apache.daffodil.tdml

trait TdmlSuite {
  def path(): String

  def parserTestNames(): Seq[String] = Seq(
    "booleanDefault",
    "booleanDefaultSDE",
    "booleanInputValueCalc"
  )

  // todo;; do you put the tdml extracting code up here?
  //        that would allow the suites to get it for free
  //        allowing the test interface to call it cleanly
}
