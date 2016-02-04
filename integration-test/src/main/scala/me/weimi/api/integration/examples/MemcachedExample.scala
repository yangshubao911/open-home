package me.weimi.api.integration.examples

import shade.memcached.{Configuration, Memcached}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-10-29 12:15
 */
object MemcachedExample extends App {
  val memcached = Memcached(Configuration("127.0.0.1:11211"), global)
  val set: Future[Unit] = memcached.set("test.test", "testvalue", 1.minute)
  println("set result: " + Await.result(set, 1 seconds))

  val get: Future[Option[String]] = memcached.get[String]("test.test")
  println("get result: " + Await.result(get, 1 seconds).getOrElse("not exists"))

  val add: Future[Boolean] = memcached.add("username", "Alex", 1.minute)
  println("add result: " + Await.result(add, 1 seconds))

  val del: Future[Boolean] = memcached.delete("test.test")
  println("del result: " + Await.result(del, 1 seconds))
}
