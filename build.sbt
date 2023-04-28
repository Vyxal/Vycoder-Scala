//TODO clean this file up
// enablePlugins(ScalaJSPlugin)

val vyncodeVersion = "1.0.0"

ThisBuild / scalaVersion := "3.2.2"

//Automatically reload SBT when build.sbt changes
Global / onChangedBuildSource := ReloadOnSourceChanges

import org.scalajs.linker.interface.OutputPatterns

lazy val root: Project = project
  .in(file("."))
  .aggregate(vyncode.js, vyncode.jvm)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val vyncode = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(
    // Shared settings
    name := "vyncode",
    version := vyncodeVersion,
    semanticdbEnabled := true,
    libraryDependencies ++= Seq(
      // For number stuff
      // For command line parsing
      "com.github.scopt" %%% "scopt" % "4.1.0",
      // Used by ScalaTest
      "org.scalactic" %%% "scalactic" % "3.2.15",
      "org.scalatest" %%% "scalatest" % "3.2.15" % Test
    ),
    scalacOptions ++= Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-encoding",
      "utf-8", // Specify character encoding used by source files.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      // Above options from https://tpolecat.github.io/2017/04/25/scalac-flags.html
      "-language:implicitConversions",
      "-language:adhocExtensions",
      // "-explain",
      "-print-lines"
    ),
    // Configure Scaladoc
    Compile / doc / target := file("docs"),
    Compile / doc / scalacOptions ++= Seq(
      "-project-version",
      vyncodeVersion,
      "-groups", // Group similar functions
      "-Ygenerate-inkuire", // Allow type-based searches
      "-external-mappings:.*vyxal.*::scaladoc3::https://vyxal.github.io/Vyxal/docs/",
      "-external-mappings:.*scala.util.parsing.*::scaladoc3::https://scala-lang.org/api/2.12.8/scala-parser-combinators/",
      "-external-mappings:.*scala(?!.util.parsing).*::scaladoc3::https://scala-lang.org/api/3.x/",
      "-external-mappings:.*java.*::javadoc::https://docs.oracle.com/javase/8/docs/api/"
    ),
    // From https://www.scalatest.org/user_guide/using_the_runner
    // Suppress output from successful tests
    Test / testOptions += Tests
      .Argument(TestFrameworks.ScalaTest, "-oNCXELOPQRM")
  )
  .jvmSettings(
    // JVM-specific settings
    Compile / mainClass := Some("vyncode.Main"),
    assembly / mainClass := Some("vyncode.Main"),
    assembly / assemblyJarName := s"vyncode.jar",
    // Necessary for tests to be able to access src/main/resources
    Test / fork := true
  )
  .jsSettings(
    // JS-specific settings
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.4.0"
    ),
    // Where the compiled JS is output
    Compile / fastOptJS / artifactPath := baseDirectory.value.getParentFile / "pages" / "vyncode.js",
    Compile / fullOptJS / artifactPath := baseDirectory.value.getParentFile / "pages" / "vyncode.js"
  )
