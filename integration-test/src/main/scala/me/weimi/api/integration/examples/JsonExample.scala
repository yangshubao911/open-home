package me.weimi.api.integration.examples

import org.json4s.JsonAST.{JObject, JInt, JValue, JString}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-10-27 18:38
 */
object JsonExample extends App {

  val json =
    ("id" -> 12) ~
      ("name" -> "tom") ~
      ("nums" -> List(1, 2, 3, 4, 5)) ~
      ("infos" ->
        ("age" -> 30) ~
          ("desc" -> "hello") ~
          ("ext1" -> "world")) ~
      ("exts" ->
        Map("ext1" -> "e1", "ext2" -> "e2"))

  //to json string
  val jsonStr: String = compact(render(json))
  println("jsonString: " + jsonStr)


  //to json object
  val jsonObj = parse(jsonStr)
  println(jsonObj)

  //  println(compact(render(jsonObj \ "name")).getClass) //String: "tom"

  println((jsonObj \ "name").getClass) //JString

  println(JInt(2).values) //int 2
  // \ 只能取下一级的值 得到的是JValue子对象
  println(jsonObj \ "id" values) //bigInt
  println(jsonObj \ "name" values) //String
  println(jsonObj \ "nums" values) //List[1,2,3,4,5]
  println(jsonObj \ "infos" values) //Map(age -> 30, desc -> hello,world)
  println(jsonObj \ "notExists" values) //None
  println(jsonObj \ "not" \ "exist" values) //None

  // \\ 可以支持跨多级取值
  println("age: " + (jsonObj \ "age").values) //None
  println("age: " + (jsonObj \\ "age").values) //30
  println("ext1: " + (jsonObj \\ "ext1")) //直接取values得到的是最后一个的值  JObject(List((ext1,JString(world)), (ext1,JString(e1))))
  println("ext1: " + (jsonObj \\ "ext1").children) // List(JString(world), JString(e1))
  println("ext1: " + (jsonObj \\ "ext1").children(0).values)
  // List(JString(world), JString(e1))

  //动态添加参数
  val userJsonObj = new JObject(List("id" -> 1, "name" -> "tom"))
  val newUserJsonObjList: List[(String, JValue)] = userJsonObj.obj.+:("desc", JString("desc"))
  println(userJsonObj)
  println(newUserJsonObjList)
  println(new JObject(newUserJsonObjList))

}
