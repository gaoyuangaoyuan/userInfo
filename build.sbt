name := "userInfo"

version := "1.0"

scalaVersion := "2.11.8"


// http://mvnrepository.com/artifact/org.apache.spark/spark-core_2.11
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.1" % "provided"

// http://mvnrepository.com/artifact/org.apache.spark/spark-mllib_2.11
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "1.6.1" % "provided"

// http://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.11
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "1.6.1" % "provided"

libraryDependencies += "org.json" % "json" % "20160212"

libraryDependencies +=  "org.apache.hbase" % "hbase-common" % "1.2.1" excludeAll(ExclusionRule(organization = "javax.servlet", name="javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name="jetty"), ExclusionRule(organization = "org.mortbay.jetty", name="servlet-api-2.5"))

libraryDependencies +=  "org.apache.hbase" % "hbase-client" % "1.2.1" excludeAll(ExclusionRule(organization = "javax.servlet", name="javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name="jetty"), ExclusionRule(organization = "org.mortbay.jetty", name="servlet-api-2.5"))

libraryDependencies +=  "org.apache.hbase" % "hbase-server" % "1.2.1" excludeAll(ExclusionRule(organization = "javax.servlet", name="javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name="jetty"), ExclusionRule(organization = "org.mortbay.jetty", name="servlet-api-2.5"))

// http://mvnrepository.com/artifact/org.codehaus.jackson/jackson-jaxrs
libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.8.3"

libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.9.11"

// https://mvnrepository.com/artifact/redis.clients/jedis
libraryDependencies += "redis.clients" % "jedis" % "2.8.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming_2.11
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.1"  % "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka_2.11
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.1"

// https://mvnrepository.com/artifact/org.apache.kafka/kafka_2.11
libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.8.2.1"


// https://mvnrepository.com/artifact/com.rockymadden.stringmetric/stringmetric-core_2.11
libraryDependencies += "com.rockymadden.stringmetric" % "stringmetric-core_2.11" % "0.27.4"  % "provided"


// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.3"

// https://mvnrepository.com/artifact/com.norbitltd/spoiwo_2.11
libraryDependencies += "com.norbitltd" % "spoiwo_2.11" % "1.1.1"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.0" % "provided"



assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}