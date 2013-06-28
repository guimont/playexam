package controllers

import play.api._
import play.api.mvc._

import models.{Question,Questions}
import models.{Part,Parts}
import models.{Answer,Answers}

/*https://code.google.com/p/yogamamadvd/source/browse/branches/play2/main/app/models/Cart.scala?r=188*/
case class index(name:String,res:Boolean)

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

      Ok(views.html.details(id,question,Parts.findAllbyQId(id), Answers.findAllbyQId(id)))
    }.getOrElse(NotFound)
  }


  def answer(id: Long) = Action { implicit request =>
   
     Logger.info(request.body.toString);
    var next : Long = 0
    if (id>=2) next = 1
    else next = id+1
    Redirect(routes.Application.show(next))
  }
}