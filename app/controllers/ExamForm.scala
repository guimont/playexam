package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.CandidateFootprint 
import models.Candidates 
import models.{Exams,ExamFootprint}



object ExamForm extends Controller {
 
  
  def exam (id: Long ) = Action { implicit request =>
    Ok(views.html.exams.exam(Exams.findAllbyCId(id)))
  }


  val examFootprint = Form(mapping(
    "Notifier" -> text) (ExamFootprint.apply)(ExamFootprint.unapply))

  def createForm(id: Long) = Action {
    Ok(views.html.exams.create(id,examFootprint))
  }


  def create(id: Long) = Action { implicit request =>
    examFootprint.bindFromRequest.fold(
      formWithErrors => {
        Logger.info(formWithErrors.toString)
        Ok(views.html.exams.create(id,formWithErrors))},
      success = { newExam =>
        Logger.info("create exam"); 
      	Exams.insert(id,newExam)
      	Redirect(routes.CandidateForm.candidates)
      }
    )
  }

}