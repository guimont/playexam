  package controllers

  import play.api._
  import play.api.mvc._
  import play.api.data._
  import play.api.data.Forms._
  import play.api.mvc.RequestHeader

  import models.{Question,Questions}
  import models.{Part,Parts}
  import models.{Answer,Answers}
  import models.{CResult,CResults}
  import models.Exams
  import models.Tests



object Test extends Controller {

  def show(id: Long) = Action { implicit request =>
      request.session.get("SessionID").map { Sid => 
        Questions.find(id).map { question =>
          Ok(views.html.details(id,Tests.find(Exams.find(Sid.toLong).tid).nb_q,
            question,Parts.findAllbyQId(id), Answers.findAllbyQId(id)))
        }.getOrElse(Unauthorized("Oops, you are not connected"))
      }.getOrElse(Unauthorized("Oops, you are not connected"))
    }


    def answer(id: Long) = Action { implicit request =>
     
      Logger.info(request.body.toString); 
      request.session.get("SessionID").map { Sid =>
        CResults.decode(id,Sid.toInt,request.body.toString)
        
        if (Tests.find(Exams.find(Sid.toLong).tid).nb_q == id)
          Redirect(routes.Test.end)
        else
          Redirect(routes.Test.show(id+1))

      }.getOrElse(Unauthorized("Oops, you are not connected"))      
    }


    def end = Action { implicit request =>
      Ok(views.html.test.end()).withNewSession
    }
}