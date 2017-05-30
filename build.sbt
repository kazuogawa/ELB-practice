name := """play-scala-seed-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  //"com.h2database"  %  "h2"                           % "1.4.195",
  //"org.hsqldb" % "hsqldb" % "2.3.2"
  "mysql" % "mysql-connector-java"                        % "5.1.40",
  "org.scalikejdbc" %% "scalikejdbc"                      % "2.5.2",
  "org.scalikejdbc" %% "scalikejdbc-test"                % "2.5.2"   % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test",
  "org.scalikejdbc" %% "scalikejdbc-config"             % "2.5.2",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.1",
  "org.flywaydb"    %% "flyway-play"                      % "3.1.0"
)

scalikejdbcSettings
