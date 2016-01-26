package me.weimi.api.integration.utils

import java.io.File

import scala.io.Source

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-10-29 16:10
 */
sealed abstract class ENV(val api: String, val img: String)

case object DEV extends ENV("http://localhost:8897/", "http://test2.img.hiwemeet.com")

case object TEST extends ENV("http://10.0.8.102:8897/", "http://test2.img.hiwemeet.com")

case object ONLINE extends ENV("http://currency.intra.hiwemeet.com:8897/", "http://img.hiwemeet.com")

object ENV {
  private val APP_ENV_VAR = "matrix_app_env"
  private val APP_ENV_FILE = "/apps/matrix/conf/env"

  lazy val env = {
    var appEnv = System.getenv(APP_ENV_VAR)
    if (Option(appEnv).isEmpty) {
      if (new File(APP_ENV_FILE).exists()) {
        appEnv = Source.fromFile(APP_ENV_FILE, "utf8").mkString.stripLineEnd
      }
    }
    appEnv match {
      case "dev" => DEV
      case "prod" => ONLINE
      case _ => TEST
    }
  }

  def apply() = env

  lazy val apiUrl = env.api
  lazy val imgUrl = env.img

  lazy val isDev = env == DEV
  lazy val isTest = env == TEST
  lazy val isOnline = env == ONLINE
}