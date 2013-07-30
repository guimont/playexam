package controllers

import java.lang.management.ManagementFactory

import scala.concurrent.duration.DurationInt

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.mvc.{ Action, Controller, WebSocket }
import scala.language.postfixOps
import play.api.cache.Cache
import play.api.Play.current
import play.Logger

object WebSockets extends Controller {

  def statusTimer() = WebSocket.using[String] { implicit request =>
    import java.util._
    import java.text._
    val dateFormat = new SimpleDateFormat("mm ss")
    def getTimer = {
      Logger.info("getTimer")
      request.session.get("SessionID").map { Sid => 
        Logger.info("request cache: "+Sid)
        val refDate: Option[Long] = Cache.getAs[Long](Sid)
        Logger.info("date: "+(System.currentTimeMillis()-refDate.getOrElse(0L)))
        dateFormat.format(new Date(1800000L - (System.currentTimeMillis()-refDate.getOrElse(0L))))        
      }.getOrElse{
        Logger.info("no SessionID")
        "no time"}
    }

    val in = Iteratee.ignore[String]
    val out = Enumerator.generateM {
      Promise.timeout(Some(getTimer), 1 seconds)
    }

    (in, out)
  }
} 