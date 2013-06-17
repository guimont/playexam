package controllers

import play.api._
import play.api.mvc._

import models.{Answer,Answers}

object Application extends Controller {
  
   def index = Action {
    Ok(views.html.index(Answers.getAll))
  }

  /**
   * Resets the database, so that there is data to display.
   */
  def resetData = Action {

    Answers.reset()

    // TODO: replace ID values with generated sequence.
    var answers = Set(
      Answer(1, "Exo",
        "jave c'est cool ou pas"),
      Answer(2, "Exo2",
        "scala c'est plus cool non"),
      Answer(3, "Exo3",
        "le cobol c'est hype ou pas"),
      Answer(4, "Exo4",
        "le javascrip c'est dur et penible"),
      Answer(5,  "Exo5",
        "finalement le c c'est top")
    )

    answers.foreach { answer =>
      Answers.insertDB(answer)
    }

    Redirect(routes.Application.index)
  }

	def show(id: Long) = Action { implicit request =>

    Answers.findById(id).map { answer =>

      Ok(views.html.details(answer))

    }.getOrElse(NotFound)
  }
}