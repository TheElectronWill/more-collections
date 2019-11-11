import mill._
import mill.scalalib._
import mill.scalalib.publish._
import coursier.maven.MavenRepository

trait JUnitTests extends TestModule {// JUnit 5 tests
  def ivyDeps = Agg(ivy"net.aichler:jupiter-interface:0.8.3")
  def testFrameworks = Seq("net.aichler.jupiter.api.JupiterFramework")
  def repositories = super.repositories ++ Seq(
    MavenRepository("https://jcenter.bintray.com")
  )
}

object main extends Cross[MainModule]("2.12.10", "2.13.1")

class MainModule(val crossScalaVersion: String) extends CrossScalaModule with PublishModule {
  def artifactName = "more-collections"
  def publishVersion = "1.0.0"
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
  object test extends Tests with JUnitTests {}
}
