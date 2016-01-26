package me.weimi.api.integration.utils.db

import me.weimi.api.integration.utils.ENV
import scalikejdbc.{NamedDB, ConnectionPool}

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-12-12 15:32
 */
trait ForumDB {
  private val context: Symbol = 'forum
  lazy implicit val db: NamedDB = NamedDB(context)

  Class.forName("com.mysql.jdbc.Driver")
  if (ENV.isDev) {
    ConnectionPool.add(context, "jdbc:mysql://localhost:3306/forum", "hiwemeet", "db_hiwemeet_0327")
  } else if (ENV.isTest) {
    ConnectionPool.add(context, "jdbc:mysql://m_api_db.intra.hiwemeet.com:2013/forum", "hiwemeet", "db_hiwemeet_0327")
  }
}
