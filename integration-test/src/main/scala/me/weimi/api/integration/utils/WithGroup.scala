package me.weimi.api.integration.utils

import org.apache.commons.lang3.RandomUtils

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-11-04 15:51
 */
object WithGroupUtil {
  val groups = {
    if (ENV.isDev) {
      Array(140784, 140793, 140825, 140867, 140873, 140915, 140931, 141021, 141023, 141287, 141313, 141319, 141431, 141451, 141489, 141581, 141595, 141637, 141680, 141713)
    } else if (ENV.isTest) {
      Array(101037, 101053, 101064, 101085, 101092, 101096, 101210, 101250, 101256, 101269, 101301, 101320, 101324, 101328, 101378, 101394, 101451, 101524, 101536, 101543)
    } else {
      Array(2064086, 2067495, 2069565, 2082172, 2085376, 2091069, 2092674, 2094787, 2098346, 2103975, 2104625, 2124635, 2164949, 2172013, 2175259, 2179232, 2180693, 2184780, 2197804, 2325634)
    }
  }

  def randomGid = {
    groups.apply(RandomUtils.nextInt(0, groups.length))
  }

  def getUnSameGid(gids: Long*) = {
    var gid = randomGid
    while (gids.contains(gid)) {
      gid = randomGid
    }
    gid
  }
}

object withGroup {
  def apply(fun: Long => Any) = {
    fun(WithGroupUtil.randomGid)
  }
}

object with2Group {
  def apply(fun: (Long, Long) => Any) = {
    val gid1 = WithGroupUtil.randomGid
    val gid2 = WithGroupUtil.getUnSameGid(gid1)
    fun(gid1, gid2)
  }
}

object with3Group {
  def apply(fun: (Long, Long, Long) => Any) = {
    val gid1 = WithGroupUtil.randomGid
    val gid2 = WithGroupUtil.getUnSameGid(gid1)
    val gid3 = WithGroupUtil.getUnSameGid(gid1, gid2)
    fun(gid1, gid2, gid3)
  }
}