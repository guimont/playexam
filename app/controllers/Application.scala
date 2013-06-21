package controllers

import play.api._
import play.api.mvc._

import models.{Question,Questions}
import models.{TestResponse,TestResponses}

object Application extends Controller {
  
   def index = Action {
    Ok(views.html.index(Questions.getAll))
  }

  /**
   * Resets the database, so that there is data to display.
   */
  def resetData = Action {

    Questions.reset()
    TestResponses.reset()

    // TODO: replace ID values with generated sequence.
    Redirect(routes.Application.index)
  }

	def show(id: Long) = Action { implicit request =>

    Questions.findById(id).map { question =>
      

      Ok(views.html.details(question,TestResponses.findByAnswerId(id)))
    }.getOrElse(NotFound)
  }
}