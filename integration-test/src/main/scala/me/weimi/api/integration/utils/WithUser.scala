package me.weimi.api.integration.utils

import org.apache.commons.lang3.RandomUtils

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-11-04 15:19
 */
object WithUserUtil {
  def getRandomUid = {
    RandomUtils.nextLong(80000, 99999)
  }

  def getUnSameUid(uids: Long*) = {
    var uid = getRandomUid
    while (uids.contains(uid)) {
      uid = getRandomUid
    }
    uid
  }
}

object withUser {
  def apply(fun: Long => Any) = {
    fun(WithUserUtil.getRandomUid)
  }
}

object with2User {
  def apply(fun: (Long, Long) => Any) = {
    val uid1 = WithUserUtil.getRandomUid
    val uid2 = WithUserUtil.getUnSameUid(uid1)
    fun(uid1, uid2)
  }
}

object with3User {
  def apply(fun: (Long, Long, Long) => Any) = {
    val uid1 = WithUserUtil.getRandomUid
    val uid2 = WithUserUtil.getUnSameUid(uid1)
    val uid3 = WithUserUtil.getUnSameUid(uid1, uid2)
    fun(uid1, uid2, uid3)
  }
}

object with4User {
  def apply(fun: (Long, Long, Long, Long) => Any) = {
    val uid1 = WithUserUtil.getRandomUid
    val uid2 = WithUserUtil.getUnSameUid(uid1)
    val uid3 = WithUserUtil.getUnSameUid(uid1, uid2)
    val uid4 = WithUserUtil.getUnSameUid(uid1, uid2, uid3)
    fun(uid1, uid2, uid3, uid4)
  }
}

object with5User {
  def apply(fun: (Long, Long, Long, Long, Long) => Any) = {
    val uid1 = WithUserUtil.getRandomUid
    val uid2 = WithUserUtil.getUnSameUid(uid1)
    val uid3 = WithUserUtil.getUnSameUid(uid1, uid2)
    val uid4 = WithUserUtil.getUnSameUid(uid1, uid2, uid3)
    val uid5 = WithUserUtil.getUnSameUid(uid1, uid2, uid3, uid4)
    fun(uid1, uid2, uid3, uid4, uid4)
  }
}