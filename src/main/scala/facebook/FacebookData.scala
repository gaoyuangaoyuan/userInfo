package facebook

/**
  * Created by xm002 on 16/5/18.
  */

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

import java.text.SimpleDateFormat
import java.util.Calendar


object FacebookData {

  def loadFBData(sqlContext: SQLContext, path: String, ts_upper: String, ts_lower:String) = {

    val jdbcDF = sqlContext.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://172.31.15.181:3306/keyboard?user=mosh&password=123456",
        "dbtable" -> "t_sdk_user_info",
        "driver" -> "com.mysql.jdbc.Driver"
      )
    ).load()

    jdbcDF.registerTempTable("names")

//    val sqlcmd = "select id,device_uid,account_id,account_info from names where  create_time > '" + ts_lower + "' and create_time < '" + ts_upper + "'"

//    val sqlcmd = "select id,device_uid,account_id,account_info, package_name from names where account_info != \"\" and create_time < '" + ts_upper + "'"

    val sqlcmd = "select id,device_uid,account_id,account_info, package_name from names limit 10"

    println("gyy-log sqlcmd " + sqlcmd)
    val jdbc = jdbcDF
      .sqlContext.sql(sqlcmd)
      .map{x =>
        var accountinfo = ""
        try
        {
          val pkey = RSA.readPrivateKey(RSA.privateKey).get
          accountinfo = RSA.decrypt(x(3).toString(), pkey)
        }
        catch
          {
            case _: Throwable =>
              accountinfo = ""
          }

        x(0) + "\t" +  x(1) + "\t" + x(2) + "\t" + accountinfo + "\t" +  x(4)
      }

    println(s"#### count ${jdbc.count()}")
//    val pathx = "hdfs:///gaoy/" + ts_upper + "/"

    val pathx = "hdfs:///gaoy/facebook/"
    HDFS.removeFile(pathx)

    jdbc
      .repartition(1)
      .saveAsTextFile(pathx)

  }


  def findMaxID(sc: SparkContext,path: String):Int = {

    val maxID = sc.textFile(path)
      .map{x =>

        val item = x.split(",")
        item(0).substring(1).toInt
      }
      .takeOrdered(2)(Ordering[Int].reverse)(0)

    return maxID


  }
  def main(args: Array[String]) {

    val sc = new SparkContext()
    val sqlContext = new SQLContext(sc)

    val hadoopConf = sc.hadoopConfiguration
    val awsAccessKeyId = args(0)
    val awsSecretAccessKey = args(1)

    hadoopConf.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
    hadoopConf.set("fs.s3n.awsAccessKeyId", awsAccessKeyId)
    hadoopConf.set("fs.s3n.awsSecretAccessKey", awsSecretAccessKey)

    val calToday = Calendar.getInstance()
    val today = new SimpleDateFormat("yyyy-MM-dd").format(calToday.getTime())

    calToday.add(Calendar.DATE, -7)
    val lastweek = new SimpleDateFormat("yyyy-MM-dd").format(calToday.getTime())

//    val path = "s3n://xinmei-dataanalysis/fb/" + today
//    println("gyy-log path " + path)
//    AWS.removeFile("xinmei-dataanalysis","fb/" + today)

    val path = "/gaoy/facebook/"

    loadFBData(sqlContext, path,today, lastweek)

    sc.stop()

  }

}
