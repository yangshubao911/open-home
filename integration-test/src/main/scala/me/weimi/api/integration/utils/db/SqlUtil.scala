package me.weimi.api.integration.utils.db

import scalikejdbc.{NamedDB, DB}

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-12-12 16:22
 */
trait SqlUtil {
  def exesql[A](sql: scalikejdbc.SQL[A, scalikejdbc.NoExtractor])(implicit db: NamedDB) = {
    db autoCommit { implicit session =>
      sql.update().apply()
    }
  }
}
