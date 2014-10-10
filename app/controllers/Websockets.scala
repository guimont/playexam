package controllers

import scala.concurrent.duration.DurationInt

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.mvc.{ Controller, WebSocket, RequestHeader }
import scala.language.postfixOps
import play.api.cache.Cache
import play.api.Play.current
import java.util._
import java.text._


/**
 * Send time left
 * Date 0000 means end of test
 */
object WebSockets extends Controller {

  def getTimer(request: RequestHeader) = {
      val dateFormat = new SimpleDateFormat("mmss")
      request.session.get("SessionID").map { Sid => 
        val refDate: Option[Long] = Cache.getAs[Long](Sid)
        val u = System.currentTimeMillis()-refDate.getOrElse(0L)
        if ((1800000L - u) <= 0)
          dateFormat.format(new Date(0))
        else
          dateFormat.format(new Date(1800000L - (u)))
      }.getOrElse{"no time"}
    }

  def statusTimer() = WebSocket.using[String] { implicit request =>
    val in = Iteratee.ignore[String]
    val out = Enumerator.generateM {
      Promise.timeout(Some(getTimer(request)), 1 seconds)
    }
    (in, out)
  }
} 