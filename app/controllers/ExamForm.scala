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
 
  
  def exam (cid: Long ) = Action { implicit request =>
    //questions // cresult
    val e = Exams.findbyCId(cid)
    Ok(views.html.exams.exam(e, Questions.findAllbyTid(e.tid) , CResults.findAllbyEid(e.id.getOrElse(0)) ))
  }


  val examFootprint = Form(mapping(
    "Notifier" -> text,
    "tid" -> longNumber) (ExamFootprint.apply)(ExamFootprint.unapply))

  def createForm(id: Long) = Action {
    Ok(views.html.exams.create(id,examFootprint,TestName.options))
  }


  def create(cid: Long) = Action { implicit request =>
    examFootprint.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.exams.create(cid,formWithErrors,TestName.options))},
      success = { newExam =>
        Logger.info("create exam"); 
      	Exams.insert(cid,newExam)
      	Redirect(routes.ExamForm.launch(cid))
      }
    )
  }


  def launch(cid: Long) = Action { implicit request =>   
    Exams.findbyCId(cid).token.map { token=>
      Ok(views.html.exams.launch( token)) 
    } .getOrElse(Redirect(routes.CandidateForm.candidates)) 
  }


   def delete(id: Long) = Action { implicit request =>   
    Exams.delete(id)
    Redirect(routes.CandidateForm.candidates)
  }

}