package controllers

import java.lang.management.ManagementFactory

import scala.concurrent.duration.DurationInt

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.mvc.{ Action, Controller, WebSocket }
import scala.language.postfixOps

object WebSockets extends Controller {

  def statusTimer() = WebSocket.using[String] { implicit request =>
    import java.util._
    import java.text._
    def getTimer = {
      val dateFormat = new SimpleDateFormat("mm ss")
      dateFormat.format(new Date)
    }

    val in = Iteratee.ignore[String]
    val out = Enumerator.generateM {
      Promise.timeout(Some(getTimer), 1 seconds)
    }

    (in, out)
  }
} 