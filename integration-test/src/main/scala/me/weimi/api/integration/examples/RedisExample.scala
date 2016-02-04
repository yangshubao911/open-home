package me.weimi.api.integration.examples

import akka.util.ByteString
import redis._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * rediscala url: https://github.com/etaty/rediscala
 * @author sofn 
 * @version 1.0 Created at: 2014-10-28 13:28
 */
object RedisExample extends App {
  implicit val akkaSystem = akka.actor.ActorSystem()

  implicit val byteStringFormatter = new ByteStringFormatter[String] {
    def serialize(data: String): ByteString = {
      ByteString(data)
    }

    def deserialize(bs: ByteString): String = {
      bs.utf8String
    }
  }

  val redis = RedisClient()

  //异步
  val futurePong = redis.ping()
  println("Ping sent!")
  futurePong.map(pong => {
    println(s"Redis replied with a $pong")
  })
  Await.result(futurePong, 5 seconds)

  //pool
  val pool = RedisClientPool(List(RedisServer()), "redisPool")
  Await.result(pool.set("test.test", "value"), 1 seconds)

  val futurePong2 = pool.get("test.test")
  val value = Await.result(futurePong2, 5 seconds)
  println("get result : " + value.get)

  //Blocking 阻塞方法只支持: lpop rpop rpopplush
  Await.result(pool.lpush("test.list", "value"), 1 seconds)
  var redisBlocking = RedisBlockingClient()
  val bvalue = redisBlocking.blpop(Seq("test.list"), 1 seconds)
  println("blocking value: " + bvalue.map(result => {
    result.map {
      case (key, work) => println(s"list $key has work : $work")
    }
  }))

  akkaSystem.shutdown()
}
