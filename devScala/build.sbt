lazy val commonSettings = Seq(
  organization := "com.nomis",
  version := "0.1.0",
  scalaVersion := "2.10.4"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "devScala",
    libraryDependencies ++= Seq(
      "org.apache.spark" % "spark-core_2.10" % "1.5.1" % "provided",
      "org.apache.spark" % "spark-sql_2.10" % "1.5.1" % "provided"
    ),
    unmanagedJars in Compile += file("lib/model-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar"),
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
    assemblyJarName in assembly := "empDept.jar",
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
  )