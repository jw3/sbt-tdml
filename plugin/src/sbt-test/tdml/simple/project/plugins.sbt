sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("org.apache.daffodil" % "sbt-tdml" % x)
  case None => addSbtPlugin("org.apache.daffodil" % "sbt-tdml" % "0.1")
}
