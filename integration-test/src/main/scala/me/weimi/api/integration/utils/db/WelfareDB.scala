package me.weimi.api.integration.utils.db

import me.weimi.api.integration.utils.ENV
import scalikejdbc.{NamedDB, ConnectionPool}

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-11-25 11:29
 */
trait WelfareDB {
  private val context: Symbol = 'welfare
  lazy implicit val db: NamedDB = NamedDB(context)

  Class.forName("com.mysql.jdbc.Driver")
  if (ENV.isDev) {
    ConnectionPool.add(context, "jdbc:mysql://localhost:3306/shihui_welfare", "hiwemeet", "db_hiwemeet_0327")
  } else if (ENV.isTest) {
    ConnectionPool.add(context, "jdbc:mysql://m_shihui_db.intra.hiwemeet.com:3109/shihui_welfare", "root", "weixiaoqu123@weimi.me")
  }
}


