package org.apache.daffodil.tdml

import scala.xml.{Elem, XML}

trait TdmlSuite {
  def path(): String

  private def tdml: Elem = XML.load(getClass.getClassLoader.getResource(path()))
  def parserTestNames(): Seq[String] = {
    for (f @ <tdml:parserTestCase>{_*}</tdml:parserTestCase> <- tdml.child) yield (f \ "@name").text
  }
}
