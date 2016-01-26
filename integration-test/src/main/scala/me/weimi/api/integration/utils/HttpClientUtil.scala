package me.weimi.api.integration.utils

import java.io.InterruptedIOException

import org.apache.commons.lang3.StringUtils
import org.apache.http.Consts
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpRequestBase}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.message.{BasicHeader, BasicNameValuePair}
import org.apache.http.util.EntityUtils
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-10-24 18:31
 */
class HttpClientUtil {
  val testHost: String = ENV.apiUrl
  val testImgHost: String = ENV.imgUrl
  val log = LoggerFactory.getLogger(this.getClass.getName)

  var defalutTryNum = 3
  //超时时间：毫秒
  var defalutTimeOut = 20000
  val defaultHeader: Map[String, Any] = Map("X-Matrix-UID" -> 1000)
  val defaultParam: Map[String, Any] = Map()

  val httpClient: CloseableHttpClient = HttpClients.createDefault

  lazy val giveTimeOut = RequestConfig.custom()
    .setConnectionRequestTimeout(defalutTimeOut)
    .setConnectTimeout(defalutTimeOut)
    .setSocketTimeout(defalutTimeOut).build()

  def handleHeader(httpMethod: HttpRequestBase, headers: Map[String, Any]) = headers.foreach { case (k: String, v: Any) => httpMethod.addHeader(new BasicHeader(k, v.toString))}

  def headleUrl(url: String) =
    if (StringUtils.startsWith(url, "http")) url
    else if (StringUtils.startsWith(url, "/")) testHost + url.substring(1)
    else testHost + url


  def get(url: String, params: Map[String, Any] = defaultParam, headers: Map[String, Any] = defaultHeader, retry: Boolean = true, printErrorLog: Boolean = true): Option[JValue] = {
    val urlBuilder = new StringBuilder(headleUrl(url))
    if (params.iterator.nonEmpty) {
      if (!StringUtils.contains(url, "?")) {
        urlBuilder.append("?")
      }
      params.foreach { case (k: String, v: Any) => urlBuilder.append(k + "=" + v.toString + "&")}
      urlBuilder.deleteCharAt(urlBuilder.length - 1)
    }

    val httpGet = new HttpGet(urlBuilder.toString())
    httpGet.setConfig(giveTimeOut)

    handleHeader(httpGet, headers)

    log.info("get url: " + urlBuilder.toString() + " headers: " + headers)

    executor(httpGet, retry, printErrorLog)
  }

  def post(url: String, params: Map[String, Any] = defaultParam, headers: Map[String, Any] = defaultHeader, retry: Boolean = true, printErrorLog: Boolean = true): Option[JValue] = {
    val realUrl = headleUrl(url)
    val httpPost = new HttpPost(realUrl)
    httpPost.setConfig(giveTimeOut)

    handleHeader(httpPost, headers)

    val formParams = params.map { case (k: String, v: Any) => new BasicNameValuePair(k, v.toString)}

    val entity = new UrlEncodedFormEntity(formParams.toList, Consts.UTF_8)

    httpPost.setEntity(entity)

    log.info("post url: " + realUrl + " params: " + params + " headers: " + headers)

    executor(httpPost, retry, printErrorLog)
  }

  def executor(method: HttpRequestBase, retry: Boolean, printErrorLog: Boolean = true, retryNum: Int = 1): Option[JValue] = {
    var result: Option[JValue] = None

    val methodStr = getMethodName(method)

    try {
      val response = httpClient.execute(method)
      val responseEntity = response.getEntity
      if (responseEntity != null) {
        result = Option(parse(EntityUtils.toString(responseEntity)))
      }
      response.close()
    } catch {
      case e: InterruptedIOException =>
        if (retry && retryNum < this.defalutTryNum)
          return executor(method, retry, retryNum = retryNum + 1)
      case e: Exception =>
        log.error(methodStr + " error", e)
    }

    if (result.isEmpty) {
      log.error(methodStr + " not got any data!")
      if (retry && retryNum < this.defalutTryNum)
        return executor(method, retry, retryNum = retryNum + 1)
    } else if ((result.get \ "apistatus" values) != 1) {
      if (printErrorLog)
        log.warn(methodStr + " got result: \r\n" + pretty(render(result.get)))
    } else {
      result = Option(result.get \ "result")
    }

    result
  }

  val getMethodName: HttpRequestBase => String = {
    case x: HttpGet => "http get"
    case x: HttpPost => "http post"
  }

}
