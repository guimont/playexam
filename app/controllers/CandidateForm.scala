package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.CandidateFootprint 
import models.Candidates 
import models.Exams

object CandidateForm extends Controller {
  
  val candidateForm = Form(mapping(
    "firstname" -> nonEmptyText,
    "lastname" -> nonEmptyText,
    "date" -> nonEmptyText)(CandidateFootprint.apply)(CandidateFootprint.unapply))

  def createForm() = Action {
    Ok(views.html.candidate.create(candidateForm))
  }
  
  def create() = Action { implicit request =>
    candidateForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.candidate.create(formWithErrors)),
      success = { newCandidate =>
      	Candidates.insert(newCandidate)
      	Ok(views.html.candidate.candidate(Candidates.findAll, Exams.findAll))
      }
    )
  }


  def candidates = Action { implicit request =>
    Ok(views.html.candidate.candidate(Candidates.findAll, Exams.findAll))
  }

  
}