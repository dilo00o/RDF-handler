name := "RdfParser"
version := "1.0"
scalaVersion := "2.11.7"
assemblyJarName in assembly := "RdfParser.jar"

libraryDependencies ++= Seq(
	"org.apache.jena" % "jena-elephas-io" % "0.9.0",
	"org.apache.jena" % "jena-core" % "3.0.1",
	//"org.apache.jena" % "jena-arq" % "2.9.3",
	"com.github.scopt" %% "scopt" % "3.4.0")
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
	case PathList("javax", "servlet", xs@_*) => MergeStrategy.last
	case PathList("javax", "activation", xs@_*) => MergeStrategy.last
	case PathList("org", "apache", xs@_*) => MergeStrategy.last
	case PathList("com", "google", xs@_*) => MergeStrategy.last
	case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last
	case PathList("com", "codahale", xs@_*) => MergeStrategy.last
	case PathList("com", "yammer", xs@_*) => MergeStrategy.last
	case "about.html" => MergeStrategy.rename
	case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
	case "META-INF/mailcap" => MergeStrategy.last
	case "META-INF/mimetypes.default" => MergeStrategy.last
	case "plugin.properties" => MergeStrategy.last
	case "log4j.properties" => MergeStrategy.last
	case x => old(x)
}
}