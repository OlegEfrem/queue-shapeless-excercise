name := "queue-shapeless"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

// scalastyle config
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

lazy val testScalastyle = taskKey[Unit]("testScalastyle")

compileScalastyle := scalastyle.in(Compile).toTask("").value

testScalastyle := scalastyle.in(Test).toTask("").value

(compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value

(test in Test) := ((test in Test) dependsOn testScalastyle).value

// wartremover config

// code coverage configuration
coverageEnabled := true

coverageHighlighting := true

coverageMinimum := 100

coverageFailOnMinimum := true