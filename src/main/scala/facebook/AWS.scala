package facebook

/**
  * Created by xm002 on 16/5/18.
  */
import java.io.IOException
import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.DeleteObjectRequest

/**
  * @author xm002
  */
object AWS {

  //  private val bucketName = "xinmei-dataanalysis"


  def removeFile(bucketName:String,keyName:String)
  {

    val s3Client = new AmazonS3Client(new ProfileCredentialsProvider())


    s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName))

    val content = s3Client.listObjects(bucketName, keyName).getObjectSummaries()

    for(i <- 0 until content.size())
    {
      println("gyy-log: " + i)
      val file = content.get(i)
      s3Client.deleteObject(bucketName, file.getKey());
    }

    println("gyy-log: delete" )
  }

}


