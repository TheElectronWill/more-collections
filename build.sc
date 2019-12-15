import mill._
import mill.scalalib._
import mill.scalalib.publish._

trait ScalaTest extends TestModule {
  def ivyDeps = Agg(ivy"org.scalatest::scalatest:3.1.0")
  def testFrameworks = Seq("org.scalatest.tools.Framework")
}

object main extends Cross[MainModule]("2.12.10", "2.13.1")

class MainModule(val crossScalaVersion: String) extends CrossScalaModule with PublishModule {
  def artifactName = "more-collections"
  def publishVersion = "1.0.1"
  def scalacOptions = Seq("-deprecation", "-feature")

  def pomSettings = PomSettings(
    description = "Additional collections for Scala",
    organization = "com.electronwill",
    url = "https://github.com/TheElectronWill/more-collections",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("TheElectronWill", "more-collections"),
    developers = Seq(
      Developer("TheElectronWill", "Guillaume Raffin", "https://electronwill.com")
    )
  )
  object test extends Tests with ScalaTest {}
}
