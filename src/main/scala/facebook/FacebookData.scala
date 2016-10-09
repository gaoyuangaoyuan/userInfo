package facebook

/**
  * Created by xm002 on 16/5/18.
  */

import java.net.URLDecoder

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SparkSession
import java.text.SimpleDateFormat
import java.util.Calendar


object FacebookData {

  def loadFBData(sc: SparkContext, path: String, ts_upper: String, ts_lower:String) = {

    val sqlContext = SparkSession.builder().getOrCreate()

    val str = "jdbc:mysql://172.31.15.181:3306/keyboard?user=readuser&password=Rju#Mc9h5%"
    val haha = URLDecoder.decode(str.replaceAll("%", "%25"), "UTF-8")

    import sqlContext.implicits._
    val jdbcDF = sqlContext.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://172.31.15.181:3306/keyboard?user=readuser&password=Rju#Mc9h5%25",
        "dbtable" -> "t_sdk_user_info",
        "driver" -> "com.mysql.cj.jdbc.Driver"
      )
    ).load()

    jdbcDF.createOrReplaceTempView("names")


    val sqlcmd = "select id,device_uid,account_id,account_info, package_name from names where create_time > '2015-10-10 00:00:00'"

    println("gyy-log sqlcmd " + sqlcmd)
    val jdbc = jdbcDF
      .sqlContext.sql(sqlcmd)
      .repartition(1000)
      .rdd
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

        x(0) + "\t" +  x(1) + "\t" + x(2) + "\t" +  x(4) + "\t" + accountinfo
      }.cache()

    println(s"#### count ${jdbc.count()}")
//    val pathx = "hdfs:///gaoy/" + ts_upper + "/"

    val pathx = "hdfs:///gaoy/facebook/"
    HDFS.removeFile(pathx)

    jdbc
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

    val calToday = Calendar.getInstance()
    val today = new SimpleDateFormat("yyyy-MM-dd").format(calToday.getTime())

    calToday.add(Calendar.DATE, -7)
    val lastweek = new SimpleDateFormat("yyyy-MM-dd").format(calToday.getTime())

    val path = "/gaoy/facebook/"

    loadFBData(sc, path,today, lastweek)

    sc.stop()

  }

}
