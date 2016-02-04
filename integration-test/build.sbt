name := "currency-integration-test"

organization := "shihui.com"

version := "1.0"

scalaVersion := "2.11.7"

transitiveClassifiers in Global := Seq("")

credentials += Credentials("Sonatype Nexus Repository Manager", "repo.hiwemeet.com", "jenkins", "meS07aJesVwZT3KGEY2H")

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.url("Nexus hiwemeet", url("http://repo.hiwemeet.com/nexus/content/groups/public")),
  "Nexus osc" at "http://maven.oschina.net/content/groups/public/",
  "Maven Central" at "http://repo1.maven.org/maven2/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases/",
  "rediscala" at "http://dl.bintray.com/etaty/maven",
  "Spy" at "http://files.couchbase.com/maven2/"
)

//tool
libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.11" excludeAll ExclusionRule(organization = "org.scala-lang"),
  "org.json4s" %% "json4s-jackson" % "3.2.11",
  "org.apache.httpcomponents" % "httpclient" % "4.5",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "org.codehaus.janino" % "janino" % "2.7.8"
)

//db
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.2",
  "com.etaty.rediscala" %% "rediscala" % "1.4.1",
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.8",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.bionicspirit" %% "shade" % "1.6.0" exclude("spy", "spymemcached"),
  "net.spy" % "spymemcached" % "2.11.4"
)

//test
libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)
