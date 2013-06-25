package controllers

import play.api._
import play.api.mvc._

import models.{Question,Questions}
import models.{Answer,Answers}

object Application extends Controller {
  
   def index = Action {
    Ok(views.html.index(Questions.findAll))
  }

  /**
   * Resets the database, so that there is data to display.
   */
  def resetData = Action {

    Questions.reset()
    Answers.reset()

    // TODO: replace ID values with generated sequence.
    Redirect(routes.Application.index)
  }

	def show(id: Long) = Action { implicit request =>

    Questions.find(id).map { question =>
      

      Ok(views.html.details(question,Answers.findAllbyQId(id)))
    }.getOrElse(NotFound)
  }
}