package me.weimi.api.welfarecode

import java.util.Date

import me.weimi.api.integration.utils.db.{SqlUtil, WelfareDB}
import me.weimi.api.integration.utils.{HttpClientUtil, withUser}
import me.weimi.api.test.BaseSuite
import org.scalatest.FunSuite
import org.slf4j.LoggerFactory
import scalikejdbc._

import scala.util.Random


/**
 * @author sofn 
 * @version 1.0 Created at: 2014-10-22 17:04
 */
class welfareCodeTest extends BaseSuite with WelfareDB {

  /*val log = LoggerFactory.getLogger(this.getClass.getName)

  test("create welfare code") {
    HttpClientUtil.post("sh/welfare_code/create", Map("welfare_id" -> "100"), printErrorLog = false)
    val results = HttpClientUtil.post("sh/welfare_code/create", Map("welfare_id" -> "100"), printErrorLog = false)
    assert(results.isDefined)
    val json = results.get
    assertResult(22412)(json \\ "error_code" values)
  }

  test("get welfare code info") {
    val results = HttpClientUtil.get("sh/welfare_code/info", Map("welfare_id" -> "100"))
    assert(results.isDefined)
    val json = results.get
    assertResult(100)(json \\ "welfare_id" values)
  }

  test("update welfare code") {
    var info = HttpClientUtil.get("sh/welfare_code/info", Map("welfare_id" -> "100"))
    assert(info.isDefined)

    val random = new Random()
    val newStatus = random.nextInt(2)
    val newStartTime = random.nextInt(100000000) + 10
    val newEndTime = new Date().getTime / 1000 + 10000L
    val param = Map("welfare_id" -> 100, "status" -> newStatus, "start_time" -> newStartTime, "end_time" -> newEndTime)
    val results = HttpClientUtil.post("sh/welfare_code/update", params = param)
    assert(results.isDefined)
    assertResult(true)(results.get values)

    info = HttpClientUtil.get("sh/welfare_code/info", Map("welfare_id" -> "100"))
    assert(info.isDefined)
    val json = info.get
    assertResult(100)(json \\ "welfare_id" values)
    assertResult(newStatus)(json \\ "status" values)
    assertResult(newStartTime)(json \\ "start_time" values)
    assertResult(newEndTime)(json \\ "end_time" values)
  }

  test("use welfarecode") {
    withUser {
      uid =>
        exesql(sql"UPDATE wm_welfare_code_log SET user_id=0 WHERE user_id=${uid}")

        val info = HttpClientUtil.get("sh/welfare_code/info", Map("welfare_id" -> "100"))
        assert(info.isDefined)

        val newCount = (info.get \\ "send_count" values).asInstanceOf[BigInt] + 2

        val param = Map("welfare_id" -> 100, "status" -> 1, "count" -> newCount, "end_time" -> (new Date().getTime / 1000 + 1000L))
        val results = HttpClientUtil.post("sh/welfare_code/update", params = param)
        assert(results.isDefined)
        assertResult(2)(results.get.children.length)

        //验证正常使用
        val welfareCode: String = results.get.children(0).values.asInstanceOf[String]
        log.info("prepare virify welfare_code: " + welfareCode)
        assert(welfareCode.length > 0)
        val useParam = Map("welfare_code" -> welfareCode)
        val useResult = HttpClientUtil.post("sh/welfare_code/use", params = useParam, headers = Map("X-Matrix-UID" -> uid))
        assert(useResult.isDefined)
        val useJson = useResult.get
        assertResult(1)(useJson \ "status" values)
        assertResult(100)(useJson \ "welfare" \ "id" values)
        assert((useJson \ "log_id" values).asInstanceOf[BigInt] > 0)

        //验证同一个人重复领取
        val welfareCode2: String = results.get.children(1).values.asInstanceOf[String]
        log.info("prepare virify welfare_code: " + welfareCode2)
        val useParam2 = Map("welfare_code" -> welfareCode2)
        val useResult2 = HttpClientUtil.post("sh/welfare_code/use", params = useParam2, headers = Map("X-Matrix-UID" -> uid))
        assert(useResult2.isDefined)
        val useJson2 = useResult2.get
        assertResult(6)(useJson2 \ "status" values)
    }
  }*/

}
