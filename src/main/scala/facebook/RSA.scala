package facebook

/**
  * Created by xm002 on 16/5/18.
  */
import java.security._

import scala.collection.mutable.ArrayBuffer

//import java.security.interfaces.RSAPrivateKey
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import javax.crypto._

import org.apache.commons.codec.binary.Base64

object RSA {

  val privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK5MBSAZTApwLbjlR0hmS2tT5d9ZD9vpM6bRE425JJI0IOrd9tNfblSe/EBCLoLjp+G2q5GMigjPvsyKGqYyVetI89OU7b3VWOaokXAAyUxt4SEpK06kTOUCO5N36yqHtLgCgOuAJ23zxQDikTolQ9xDr/uaCoSr0Xc/IHuuYyNBAgMBAAECgYBMAPZTMvTHsHcfo0tcNM86dBFPPaT+vkg5u/4M1Okvn++iuzvAnGKeH93bn4Bsrx0r/d3+0Av5H64Rvz8zrcts35cpSYpAiThVnP+ZwlSIyb1CW7UoplhjFAj2vUDe6MJcxBTbcFs/Co4eokqocMWBd+zZuMRMAP6B5n5NVYI4CQJBAN9FleCyD2agrhWGCUmHfdDa3dEqvgNPNLOAaW0LGzdEepSXANISYP/j2LkYoZ32if+7c16jQC6xPkc3yhGSINcCQQDH2J9Eg4qR07JNBLY6Jdkp0y8bqnVCAnWhFww/Fena/QuMLVIPyg9kGBy1xtPxVCUoioJXVYYe+S0CcRIuTCGnAkBELHAuMtBnCsr9AXdWf0uYeSvf0UhBpG64HI6UYB7ISl2Pf2gdvMTnfX/QTMgUfpH2hxscG4beevpELrHBmbfzAkEAmlyjIKuuE9d9qGnUS3PCmsDJaUgvzC3UJ/qCdhP6DzFXgw3ajeGYI8tQGcQbHxPMo9Tguo6Py0rSTkmCLoqVjwJAEUXJfO48Stv1pXrfIh04KFIoS+wPxLHPAIvY/oQvrLzegQKqzd+qh4q/5J2NascbkhqlmkltffQukz6bfTqOQg=="


  def decodePublicKey(encodedKey: String): Option[PublicKey] = {
    this.decodePublicKey(
      (new Base64()).decode(encodedKey)
    )
  }

  def decodePublicKey(encodedKey: Array[Byte]): Option[PublicKey] = {
    scala.util.control.Exception.allCatch.opt {
      val spec = new X509EncodedKeySpec(encodedKey)
      val factory = KeyFactory.getInstance("RSA")
      factory.generatePublic(spec)
    }
  }

  def readPrivateKey(privateKey: String): Option[PrivateKey] = {
    this.readPrivateKey(
      (new Base64()).decode(privateKey)
    )
  }

  def readPrivateKey(privateKey: Array[Byte]): Option[PrivateKey] = {
    scala.util.control.Exception.allCatch.opt {
      val spec = new PKCS8EncodedKeySpec(privateKey)
      //val spec = new X509EncodedKeySpec(privateKey)
      val factory = KeyFactory.getInstance("RSA")
      factory.generatePrivate(spec)
    }
  }

  def encrypt(key: PublicKey, data: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    cipher.doFinal(data)
  }

  def encryptB64(key: PublicKey, data: Array[Byte]): String = {
    (new Base64()).encodeAsString(this.encrypt(key, data))
  }

  def encryptB64(key: PublicKey, data: String): String = {
    this.encryptB64(key, data.getBytes)
  }

  // add by Chico
  def base64Decode(str: String) = {
    (new Base64()).decode(str)
  }

  def decrypt(encString: String, key: PrivateKey) = {
    //val cipher = Cipher.getInstance("RSA")
    //val cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.DECRYPT_MODE, key)
    val enBytes = base64Decode(encString)
    //.slice(0, 128)
    val list = enBytes.toList.grouped(128)
    val deBytes = for (item <- list) yield {
      cipher.doFinal(item.toArray)
    }
    //val deBytes = cipher.doFinal(enBytes)
    val result = deBytes.toArray.flatten
    new String(result, "UTF-8")

  }



  /*def decrypt(str: String) = {
    val pKey = readPrivateKey(privateKey).get
    this.decrypt(str, pKey)
  }*/

  def main (args: Array[String]) {

    val account =  "QPPqo7Ss3XBtstCm+7391toaTmF9kqAZHKWn/v0Fs9XVT5rdk4t7uRznMXSojUJ87oEV7IJAGw30qLDET791dnE5eevzvW3WNB1zAOag4uw6Eeu8uRoGcbktYeyNzfxpZ3yE4TQSLW9jIc+vGH+33/93zV38y2lfQuWYITzCdzER1XXKGprNYSo5wWnt4KXzKhllhqzsrLkBl9KWrGTavZpA/TyZB6Uuwkp4gV2uTo6EoLk58OcpHFXqpMog31RcHjoeyAPdXjVv9cCE4BSm44uwBcyZFeEEuLGbu79U32+7ocaTrXxdIHvqoVBBZOAY7ajmaof9ER6ISFqnXQLzDnoum0C8XL2Mvn44oaiNIPrI0UIOUjsvYTUbHodglyrSl1+wiVsfMD93HqHZEqu617Nb9hBx9bsgYAaCgASrzUJ9wf6/330W7Rn7dSWHAW4KMjvH+rleD7EgnZW5pUMh+o5pb5lJo55VorABSvGhXrsQQwZDFsemZpdrdi7KLeCZ"

    val private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK5MBSAZTApwLbjlR0hmS2tT5d9ZD9vpM6bRE425JJI0IOrd9tNfblSe/EBCLoLjp+G2q5GMigjPvsyKGqYyVetI89OU7b3VWOaokXAAyUxt4SEpK06kTOUCO5N36yqHtLgCgOuAJ23zxQDikTolQ9xDr/uaCoSr0Xc/IHuuYyNBAgMBAAECgYBMAPZTMvTHsHcfo0tcNM86dBFPPaT+vkg5u/4M1Okvn++iuzvAnGKeH93bn4Bsrx0r/d3+0Av5H64Rvz8zrcts35cpSYpAiThVnP+ZwlSIyb1CW7UoplhjFAj2vUDe6MJcxBTbcFs/Co4eokqocMWBd+zZuMRMAP6B5n5NVYI4CQJBAN9FleCyD2agrhWGCUmHfdDa3dEqvgNPNLOAaW0LGzdEepSXANISYP/j2LkYoZ32if+7c16jQC6xPkc3yhGSINcCQQDH2J9Eg4qR07JNBLY6Jdkp0y8bqnVCAnWhFww/Fena/QuMLVIPyg9kGBy1xtPxVCUoioJXVYYe+S0CcRIuTCGnAkBELHAuMtBnCsr9AXdWf0uYeSvf0UhBpG64HI6UYB7ISl2Pf2gdvMTnfX/QTMgUfpH2hxscG4beevpELrHBmbfzAkEAmlyjIKuuE9d9qGnUS3PCmsDJaUgvzC3UJ/qCdhP6DzFXgw3ajeGYI8tQGcQbHxPMo9Tguo6Py0rSTkmCLoqVjwJAEUXJfO48Stv1pXrfIh04KFIoS+wPxLHPAIvY/oQvrLzegQKqzd+qh4q/5J2NascbkhqlmkltffQukz6bfTqOQg=="

    /*
    $pi_key =  openssl_pkey_get_private($private_key);
$str = base64_decode($account);
$lanstr = strlen($str);
$offset = 0;
$i = 0;
$kmc = '';
while($lanstr - $offset >0)
{
        if ( $lanstr - $offset > 128)
        {
                $str2 = substr($str,$offset,128);
                openssl_private_decrypt($str2,$decrypted,$pi_key);
        }else{
                $str2 = substr($str,$offset,$lanstr);
                openssl_private_decrypt($str2,$decrypted,$pi_key);
        }
        $kmc .= $decrypted;

        $i++;

        $offset = $i * 128;



}
echo $kmc;

select * from t_sdk_user_info limit 1;
+----+----------------------------------+----------------------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+----------+---------------------+---------------------+
| id | device_uid                       | account_id                       | account_info                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | package_name        | source   | create_time         | updated_time        |
+----+----------------------------------+----------------------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+----------+---------------------+---------------------+
|  3 | affd1f0691738c3fca772d128fca7263 | 42ebd8f1a2965766dd50436cb7866ebc | XfSuj3mjzZcVLBxa619rdz0glOnU5n25SEumXGwLDKci4NajSVxIdmiSiDrqijgT/F9F94EeruAy
HRz8qHJ4+rmimxEi3/afIK0/4WvASuy3UXe89ATJy/0YozZBORviypua0qdb0mbs2kR0alJ8sBNj
GjsIRXfHXrTxl5Og/pwKc2MXi/oK5qrJnLjcuDhz6qhxFFh/fsy1msLfphlQxSgdoN/8HJUO2HQY
eMS6dcqTeHIVYBchWB5qhbx9iWFTJEbtfVGIIFJEbyyFMVwV0zPboLxbobnPhmUQSNcHufAhkUhm
JQLKzVVSXn/SltpqvZ6aFDRCYmoXSW7S4jaVBIY2OmOCz4R2AJPiL0MNjijpF7MbExwpKDbMv4G4
v+3Kz3CscaLJFJZCYDgAW3FHu/I5ayJk/FNHxHQmhz3mHd7gdFPpJt8/WySejmOZUoPj8N44b/7b
gGxL4WBFP6ldcFtZCi5jPfr0CEzZkYUrrc+RrtDP8U1rfKTcoUh4RYpz
 | com.emoji.ikeyboard | facebook | 0000-00-00 00:00:00 | 0000-00-00 00:00:00 |
+----+----------------------------------+----------------------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+----------+---------------------+---------------------+
1 row in set (0.00 sec)


     */

    val info =
      """
        |XfSuj3mjzZcVLBxa619rdz0glOnU5n25SEumXGwLDKci4NajSVxIdmiSiDrqijgT/F9F94EeruAy
        |HRz8qHJ4+rmimxEi3/afIK0/4WvASuy3UXe89ATJy/0YozZBORviypua0qdb0mbs2kR0alJ8sBNj
        |GjsIRXfHXrTxl5Og/pwKc2MXi/oK5qrJnLjcuDhz6qhxFFh/fsy1msLfphlQxSgdoN/8HJUO2HQY
        |eMS6dcqTeHIVYBchWB5qhbx9iWFTJEbtfVGIIFJEbyyFMVwV0zPboLxbobnPhmUQSNcHufAhkUhm
        |JQLKzVVSXn/SltpqvZ6aFDRCYmoXSW7S4jaVBIY2OmOCz4R2AJPiL0MNjijpF7MbExwpKDbMv4G4
        |v+3Kz3CscaLJFJZCYDgAW3FHu/I5ayJk/FNHxHQmhz3mHd7gdFPpJt8/WySejmOZUoPj8N44b/7b
        |gGxL4WBFP6ldcFtZCi5jPfr0CEzZkYUrrc+RrtDP8U1rfKTcoUh4RYpz
      """.stripMargin

    val input =
      "XfSuj3mjzZcVLBxa619rdz0glOnU5n25SEumXGwLDKci4NajSVxIdmiSiDrqijgT/F9F94EeruAyHRz8qHJ4+rmimxEi3/afIK0/4WvASuy3UXe89ATJy0YozZBORviypua0qdb0mbs2kR0alJ8sBNjGjsIRXfHXrTxl5Og/pwKc2MXi/oK5qrJnLjcuDhz6qhxFFh/fsy1msLfphlQxSgdoN/8HJUO2HQYeMS6dcqTeHIVYBchWB5qhbx9iWFTJEbtfVGIIFJEbyyFMVwV0zPboLxbobnPhmUQSNcHufAhkUhmJQLKzVVSXn/SltpqvZ6aFDRCYmoXSW7S4jaVBIY2OmOCz4R2AJPiL0MNjijpF7MbExwpKDbMv4G4v+3Kz3CscaLJFJZCYDgAW3FHu/I5ayJk/FNHxHQmhz3mHd7gdFPpJt8/WySejmOZUoPj8N44b/7bgGxL4WBFP6ldcFtZCi5jPfr0CEzZkYUrrc+RrtDP8U1rfKTcoUh4RYpz"

    val in =
      "XfSuj3mjzZcVLBxa619rdz0glOnU5n25SEumXGwLDKci4NajSVxIdmiSiDrqijgT/F9F94EeruAy\nHRz8qHJ4+rmimxEi3/afIK0/4WvASuy3UXe89ATJy/0YozZBORviypua0qdb0mbs2kR0alJ8sBNj\nGjsIRXfHXrTxl5Og/pwKc2MXi/oK5qrJnLjcuDhz6qhxFFh/fsy1msLfphlQxSgdoN/8HJUO2HQY\neMS6dcqTeHIVYBchWB5qhbx9iWFTJEbtfVGIIFJEbyyFMVwV0zPboLxbobnPhmUQSNcHufAhkUhm\nJQLKzVVSXn/SltpqvZ6aFDRCYmoXSW7S4jaVBIY2OmOCz4R2AJPiL0MNjijpF7MbExwpKDbMv4G4\nv+3Kz3CscaLJFJZCYDgAW3FHu/I5ayJk/FNHxHQmhz3mHd7gdFPpJt8/WySejmOZUoPj8N44b/7b\ngGxL4WBFP6ldcFtZCi5jPfr0CEzZkYUrrc+RrtDP8U1rfKTcoUh4RYpz"

    val pkey = readPrivateKey(private_key).get

    //val de = decrypt(input, pkey)
    /*val de = decrypt(in, pkey)
    println(s"#### decrypt: ${de}")*/

    val multi =
      """
        |XfSuj3mjzZcVLBxa619rdz0glOnU5n25SEumXGwLDKci4NajSVxIdmiSiDrqijgT/F9F94EeruAy
        |HRz8qHJ4+rmimxEi3/afIK0/4WvASuy3UXe89ATJy/0YozZBORviypua0qdb0mbs2kR0alJ8sBNj
        |GjsIRXfHXrTxl5Og/pwKc2MXi/oK5qrJnLjcuDhz6qhxFFh/fsy1msLfphlQxSgdoN/8HJUO2HQY
        |eMS6dcqTeHIVYBchWB5qhbx9iWFTJEbtfVGIIFJEbyyFMVwV0zPboLxbobnPhmUQSNcHufAhkUhm
        |JQLKzVVSXn/SltpqvZ6aFDRCYmoXSW7S4jaVBIY2OmOCz4R2AJPiL0MNjijpF7MbExwpKDbMv4G4
        |v+3Kz3CscaLJFJZCYDgAW3FHu/I5ayJk/FNHxHQmhz3mHd7gdFPpJt8/WySejmOZUoPj8N44b/7b
        |gGxL4WBFP6ldcFtZCi5jPfr0CEzZkYUrrc+RrtDP8U1rfKTcoUh4RYpz
        |
        |NUfNxBvJpJw7LzZ+4sCTBkEpYzv4e0SMwiiNE/BRrq3y7lXTe5Oe4y+uUc/vCQGUETuOHZXPVrbB
        |w4cfmf5JhPx8TkctogxMA+myx7f6ed83NKl8NNEdrSklyqNso5dh1rQa2NTQQuavJDhN0UKjTlKH
        |Nuqb2lb7mdu3cwceM9s7odnMT277p+Cw3yjdp+WDwhHrFa069ucXTgefulrVAzbqvdn/h9jxkFs0
        |gFW6W0I3kUZZhDrWeU33BQLcaeAMN34uCHzNzzRzoLk9B8FTjWOV05vDo/q8D4Yazy0kOkA64ouD
        |GH0HGb1c30RICW7p9hPlSGXgYKpLeGfaLBYKXUWaEvuFxDlPm2S9x+n/9hiXOktTIl4m1DBo1DUR
        |IMdM9grcuGBaGL3ke92/2tHZeKUIUoQczrypXOAihTURl9QjILZoJucsCk+9QhFTGnpBfwad3ATw
        |K4oqLNa0K6jYq4kdof01f60qqERd8iswLl+CL+jK83yK2vxc2wADQrch
        |
        |fiwdndeSZc4Vwf+MS/LU5akco2eD0NRNS3XupgiG4Wk7HLG6zN3YhyA8/88f4RoZ8lwwAytBcK0F
        |aL/itpUGt7JDTKyrPajbEz6FF0mjc3zCoR05fPcKAisjrZBrfVWNN1rBhnbJuUbqIObi0yQU1IJY
        |uVbzL/z5vrR6DGc3ZxuhDfbjbmXMK7/T0up/RZyiR248H3fTFGvhC4fN12Qrk6V8GLAE2vVwhUUf
        |liFNb08xpkPU719V2W6Jz3ZIM7ljD7i8ei9Amfb5U69ZWGycVzLYjK2+6ltZrbJ3dAj7PCeFaO9u
        |ZL8EbMbq9HHbeP0+2mFSxBHv0x0yrDyLw7G3D0wCKPf0TqBaCqut0aqvKvnGCZ5C7tBFukTYV1tw
        |rPBRZNqBL/nCaTxXnCjDb/rEWPPgdMVG9C/F2Hps46WeZG8Qvdb+naaLhKHuJn93ymYANa6qgXj5
        |zyLO9+A22S5CFYPQWiEQ/bU0apW1ErHfobDQtal8zPCLiYdsZE2twbXx
        |
        |W5P/qB12Z860siy0rXesrHv+HksYw3C2dhFs6tfUjEwlF28XsLZg4MhJNXJzxpWyTdXDgndeVZ9z
        |7tqi4eEqFETor8yVDlEr2X8px26Q+qPjuMpFbISsbrqvOHxLpsJCgLg9XE+NICqbCD6ZMHjhJigt
        |QwrGoiTNgxieYXmykvxuud0Mvs3D9t3DQHExDQWP8bfHqhZ1nPBISA6TvuFyydgaWp3LKiZGQMQx
        |8c7ywqrjisqlni/VRswPzn3OH+5MrDRKPWwqqn6Ae0SO8lsWhWn7U3Y2qjl+/jGrTR4cQBwlrDRD
        |2qKb8FkkR6oArj4yqmNVE+zIdbhyGrETkg6/H5iCzsPTp4rVRsVVkv7dLof2fJ7z6yRyw1cVbL81
        |jaGUdKZWJY9mmX3+VXHrAYpo8LXwUkkzflC62YGWTlPvOj5k5EeKTZaQ1zE61q3KgBAxIcvc9jrX
        |e8WTUIkL0AhqiQLRvOqD47aP7yWoCpwsEUBPC6qKNka+U9EPpgv2nOtY
        |
        |g0S2k9iyoxir2+a922cEYhVk2Q1MaFweJBVvUemozMTQf5jsLSlBRdD4ssO41oX29EKU7mx3h4j+
        |scJYRDtAU+z8euacwiPSTxQpjz6kgMn2/2qchvaB8njofm09/WkrESu9vyJaswwG27UMR2XMkZs+
        |OUCwHxrP6rRwGaMMlyJr8cqEjKMcXAbHJyZjceUy+0+imtr6A2MoEhWHpyZuZH5ABI6eck78AaNm
        |y+sGFCev7EL62NXMywVp5KPhOkbeIFoJIpv+TerGlVgpldwDF5QuUaNxWfxbKuh84ReJrSs4KkCc
        |s9W654sYWmDHhBpTbbQCamWkzUDBayalhht0kpUmrUesZoad7s9MdMhSWb3DnrZpqcL/ySF7b0nM
        |GBTaysxKKCJ3ClvycqPLfP/mc3UjmolymK5aRquawZuEq6E+km2HJJ3ttPwQmHKat9cj4pkJUH1q
        |VBKtGOtPxpB14tqo/KEvPk3BbB8TRkFI892PSsY0HnL5aGu44APHpqu4
        |
        |L208znizsKEKX8IY0IqKcNOHP1xODwtDV8J7h6MO8dNYbphyapmU8GYN8EBtMupUaX60OSP4aAbD
        |VeDhlHP6vMHMBfZr7/Xxx4C6V+Frfkl9rtuSlfeSBXRtZfjxMsWQtP5XM/FBK6YFuvSm/Z6LVamh
        |ddwzNJlbDmS7iLssIC12WNqNpueLxgl2LnX92bkipMeeC03sgcj7lDgSB4ac6N3w60Xp9QilRpC+
        |2JDve681n5i6CfRgYLDgoABDd6/IdqYDQHwgzJbANlCnS/M5m5AEDd8CSBZoHRSyd5tmDtwDeQMW
        |b9QKe0RHPRjnygwX/oG3bY6F3hvqNzso9MLyQFNo6cBkq332waj2FIQ2xvXKBHsKOKmwfL++QHPp
        |B3zCLBtgZ8ewYPscThXAOfJqoB8ISm+IkUqO3nlyZfljOjLhd/+NbgUGnzg6mwUzBggOjsDSJjkf
        |KQjIx9gZcuHdfMImJKE7Cp0KAXYPWUtvTBRb7mHIpX0NEim2SWy/Ow9B
        |
        |QPPqo7Ss3XBtstCm+7391toaTmF9kqAZHKWn/v0Fs9XVT5rdk4t7uRznMXSojUJ87oEV7IJAGw30
        |qLDET791dnE5eevzvW3WNB1zAOag4uw6Eeu8uRoGcbktYeyNzfxpZ3yE4TQSLW9jIc+vGH+33/93
        |zV38y2lfQuWYITzCdzER1XXKGprNYSo5wWnt4KXzKhllhqzsrLkBl9KWrGTavZpA/TyZB6Uuwkp4
        |gV2uTo6EoLk58OcpHFXqpMog31RcHjoeyAPdXjVv9cCE4BSm44uwBcyZFeEEuLGbu79U32+7ocaT
        |rXxdIHvqoVBBZOAY7ajmaof9ER6ISFqnXQLzDnoum0C8XL2Mvn44oaiNIPrI0UIOUjsvYTUbHodg
        |lyrSl1+wiVsfMD93HqHZEqu617Nb9hBx9bsgYAaCgASrzUJ9wf6/330W7Rn7dSWHAW4KMjvH+rle
        |D7EgnZW5pUMh+o5pb5lJo55VorABSvGhXrsQQwZDFsemZpdrdi7KLeCZ
        |
        |nit23zCVDQ3M9CvRxHOTpKHbUSpDVB5HWY4mahi4mo3FXNygMG+Zg4G9r4GoIcVASrB//Z0behqS
        |4r76kvDpJpZAlAxpp0PBmjoFMAUzWC3/Bznpf49m+dYqWp0vhw6UqNYN4Rk+K3okpGZPMQkId+P7
        |gF1cIe+J3Yx4Y45+czRwht5XKI/ZKVLcBJkvXDX5veNKV9TJUhIcNCh3Qqf/n1ZJrcajXl1f5moI
        |3nZ0H89kW3NF28VnYvY9V+h5g3ISd6f7XqYlxA+t/eqbmviZxOc60ByUNFMRCcLXGi+3Pb2e6qsC
        |BT11ZQ7/nqPVKGXwoJX/s/CgYUlZZ5JwZnF4HD4Iy+oJy1qlMy1weJxNcFH10CBmGppxkVfQs0G/
        |htDNDBQpv4CXSU524qLZNorpRhZXSqS+TRz60+Y9gpOwkEdufjcmSjS1i4zt+xdTAWNDUyN2SEWU
        |44oZ6kcbB+22tpf5BvIpgMlBuyS/yg9EXR72oIkSxDntdRsBC7S7GaIT
        |
        |lHJoH10McxUtHw+IQ9eh260R5N3ez4ZL7++HOfTEbpibGwpL7OVagvv2A03iB1YP6ritOEnIus0I
        |QTHt4+hyGqi0KL8Knef2BviOGRmoNy9RLx+6KSGepXwjB2kwFx2K8NdDAqd383Jc7SXPyaW8TmB1
        |mOwQpJkuEUQxg1XR2UcfZ7MEJzKmwZrK/HDDtjtaTOAlCNqTGDrBKLzdJFfHynnSHQLGzUe9WxJZ
        |IMU5xRWa7IaliPr6CcuZ8+/gjvDn81bWFSG3DaX+kJRfLgYNF1Gu2l9sOriyLp3vakKMjthJFBSb
        |4QYn6q/nDnGvR+bAGHW+sItTQn3kbnpcE7Jee3/RibioVVctseKYCx6D7pgx73o5vyZ+TQIsGuVA
        |+HmveOEysBuOxgON774+NISaA2Wh009TyEcgO4bVdpGNvaWBC+QXD+z1lzq7tPkJsukvzP2/5FhM
        |/gABmKgROSiT4CQnpTejHql2gSX3OpC+5vzosvQNnyHMnxc0MNuC6BXP
        |
        |g0nIXVjrj4xU8Alc3tJlr3nK+fX+Dfq2KMkKNpmAKrYwZ5dY+XiMQ8KGpqB+ZLIXrJqwY4a40N9c
        |lCoo+EyfRKAbdHqJmkfjsAxiz4i1CYVU5rEwfUnBMrDTUjqfEUDbPrn5PXiAyepq5fmpn2hK+e7l
        |Pcv9mbQ0MNNknGu7iR0b5PQ8Y5rxoou2vpGsBVrd3sDb4aMfjs1ezVASUhLZvP7JYz5GPKq1iyIH
        |6Xot5pD7XDHipDwoZzZRBED3V6MJIwc7qUrpOwP27GrIrjy71v09tJ58pO+1tFld8q0o/YDfdzYD
        |wOPq+mqWuUhypdY9e2ipCyHmRm/hCk0imAo6hiTb8hM/ClYX/l0+Qnr8inCv5jZ0lCuJc8fEimrB
        |N57ymOZOZF2XLxKz+qkzDLWL60Cczkbqkbh9L5qgD+D1TesxI1Tt7jsqwUYU7F+18887nkLZiX3Q
        |H0jrX+lr3X0e2vGkeu6vrFSe2jak1mdRmZ0pDpnjmObq0uE6g9YRP0oL
      """.stripMargin.split("\\n\\n")

    for (m <- multi) {
      println(s"${decrypt(m, pkey)}\n")
    }

  }


  /*public static String encrypt(String str) {
    try {

      String source = str;
      LogForTest.logW("加密前文字：" + source);
      byte[] data = source.getBytes();
      byte[] encodedData = RSAUtils.encryptByPublicKey(data, DEFAULT_PUBLIC_KEY);
      //LogForTest.logW("加密后文字：" + new String(encodedData));
      LogForTest.logW("加密后文字：" + URLEncoder.encode(new String(Base64.encode(encodedData, Base64.DEFAULT))));

      return URLEncoder.encode(new String(Base64.encode(encodedData, Base64.DEFAULT)));

    } catch (Exception e) {

      LogForTest.logE("加密解密错误：" + e);

    }
    return null;
  }

  public static byte[] encryptByPublicKey(byte[] data, String publicKey)
  throws Exception {
    byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    // 对数据加密
    Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }

  public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
  throws Exception {
    byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段解密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return decryptedData;
  }
  */




}
