package com.shihui.test.common

import me.weimi.api.integration.utils.HttpClientUtil
import org.scalatest.FunSuite

/**
 * @author sofn 
 * @version 1.0 Created at: 2015-09-08 11:24
 */
class PingTest extends FunSuite {

  val httpclient = new HttpClientUtil

  test("testping") {
    val res = httpclient.get("/help/ping")
    assertResult(true)(res.nonEmpty)

    val json = res.get
    assertResult("OK")(json \\ "status" values)
  }
}
