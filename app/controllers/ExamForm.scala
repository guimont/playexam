package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.CandidateFootprint 
import models.Candidates 
import models.Exams

object ExamForm extends Controller {
  
  
  def createForm(id: Long) = Action {
    Ok("ok")
  }
  
  def exam (id: Long ) = Action { implicit request =>
    //Ok(views.html.exam(Exam.findAllbyCId))
    Ok("exam")
  }
}