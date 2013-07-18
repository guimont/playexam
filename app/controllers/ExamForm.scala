package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.CandidateFootprint 
import models.Candidates 
import models.{Exams,ExamFootprint}
import models.TestName



object ExamForm extends Controller {
 
  
  def exam (id: Long ) = Action { implicit request =>
    Ok(views.html.exams.exam(Exams.findAllbyCId(id)))
  }


  val examFootprint = Form(mapping(
    "Notifier" -> text,
    "tid" -> longNumber) (ExamFootprint.apply)(ExamFootprint.unapply))

  def createForm(id: Long) = Action {
    Ok(views.html.exams.create(id,examFootprint,TestName.options))
  }


  def create(id: Long) = Action { implicit request =>
    examFootprint.bindFromRequest.fold(
      formWithErrors => {
        Logger.info(formWithErrors.toString)
        Ok(views.html.exams.create(id,formWithErrors,TestName.options))},
      success = { newExam =>
        Logger.info("create exam"); 
      	Exams.insert(id,newExam)
      	Redirect(routes.ExamForm.launch(id))
      }
    )
  }


  def launch(id: Long) = Action { implicit request =>   
    Exams.findAllbyCId(id).token.map { token=>
      Ok(views.html.exams.launch( token)) 
    } .getOrElse(Redirect(routes.CandidateForm.candidates)) 
  }

}