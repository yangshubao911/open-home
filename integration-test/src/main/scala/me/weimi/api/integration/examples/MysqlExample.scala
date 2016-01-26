package me.weimi.api.integration.examples

import scalikejdbc._

/**
 * Authors: sofn
 * Version: 1.0  Created at 14-10-28 23:36.
 */
case class Task(id: Long, uid: Long, title: String, content: String)

object MysqlExample extends App {
  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/tasks", "root", "root")

  val getOne: Option[Long] = DB readOnly { implicit session =>
    sql"select 1 id"
      .map(rs => rs.long("id"))
      .single()
      .apply()
  }

  def add(uid: Long, title: String, content: String): Long = DB autoCommit { implicit seesion =>
    val result = sql"insert into tasks(uid,title,content,create_time) values(${uid},${title},${content},current_timestamp )"
      //      .update()
      //      .apply()
      //    println(result)  //都是1
      .updateAndReturnGeneratedKey().apply()
    println(result) //主键
    result
  }

  def getById(id: Long): Option[Task] = DB readOnly { implicit seesion =>
    sql"select id,uid,title,content from tasks where id=${id}"
      .map(rs => Task(rs.long("id"), rs.long("uid"), rs.string("title"), rs.string("content")))
      .single()
      .apply()
  }

  def getAll: List[Task] = DB readOnly { implicit seesion =>
    sql"select id,uid,title,content from tasks"
      .map(rs => Task(rs.long("id"), rs.long("uid"), rs.string("title"), rs.string("content")))
      .list()
      .apply()
  }

  def remove(id: Long): Boolean = DB autoCommit { implicit session =>
    sql"delete from tasks where id=${id}"
      .update()
      .apply() > 0
  }

}
