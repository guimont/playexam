package controllers

import java.lang.management.ManagementFactory

import scala.concurrent.duration.DurationInt

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.mvc.{ Action, Controller, WebSocket, RequestHeader }
import scala.language.postfixOps
import play.api.cache.Cache
import play.api.Play.current
import play.Logger
import java.util._
import java.text._

object WebSockets extends Controller {

  def getTimer(request: RequestHeader) = {
      val dateFormat = new SimpleDateFormat("mm")
      request.session.get("SessionID").map { Sid => 
        val refDate: Option[Long] = Cache.getAs[Long](Sid)
        dateFormat.format(new Date(1800000L - (System.currentTimeMillis()-refDate.getOrElse(0L))))        
      }.getOrElse{"no time"}
    }

  def statusTimer() = WebSocket.using[String] { implicit request =>
    val in = Iteratee.ignore[String]
    val out = Enumerator.generateM {
      Promise.timeout(Some(getTimer(request)), 5 seconds)
    }
    (in, out)
  }
} 