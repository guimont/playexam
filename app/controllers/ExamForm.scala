package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.CandidateFootprint 
import models.Candidates 
import models.{Exams,ExamFootprint}
import models.TestName
import models.{Question,Questions}
import models.{CResult,CResults}



object ExamForm extends Controller {
 
  
  def exam (id: Long ) = Action { implicit request =>
    //questions // cresult
    val e = Exams.findbyCId(id)
    Ok(views.html.exams.exam(e, Questions.findAllbyTid(e.tid) , CResults.findAllbyEid(id) ))
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
        Ok(views.html.exams.create(id,formWithErrors,TestName.options))},
      success = { newExam =>
        Logger.info("create exam"); 
      	Exams.insert(id,newExam)
      	Redirect(routes.ExamForm.launch(id))
      }
    )
  }


  def launch(id: Long) = Action { implicit request =>   
    Exams.findbyCId(id).token.map { token=>
      Ok(views.html.exams.launch( token)) 
    } .getOrElse(Redirect(routes.CandidateForm.candidates)) 
  }

}