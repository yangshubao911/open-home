package me.weimi.api.home

import me.weimi.api.integration.utils.{HttpClientUtil, WithGroupUtil}
import org.json4s.JsonAST.{JNothing, JValue}
import org.scalatest.FunSuite

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-11-03 12:24
 */
class appHomeTest extends FunSuite {

  /*test("test sh/app/home has must fields") {
    val results = HttpClientUtil.get("sh/app/home", Map("gid" -> WithGroupUtil.randomGid))
    results.fold {
      fail("HTTP请求失败")
    } {
      jvalue: JValue =>
        assert(jvalue \ "distance" != JNothing)
        assert(jvalue \ "ads" != JNothing)
        assert(jvalue \ "welfare" != JNothing)
        assert(jvalue \ "cates" != JNothing)
        assert(jvalue \ "city" != JNothing)
    }
  }*/

}
