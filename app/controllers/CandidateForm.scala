package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.CandidateFootprint 
import models.Candidates 
import models.CandidateExam
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
      	Redirect(routes.CandidateForm.candidates)
      }
    )
  }

/*pour chaque candidat, parcourir list*/

  def candidates = Action { implicit request =>

    var listK : Set[CandidateExam] = Set()

    Candidates.findAll.map{ candidate => 
      var n : Long = 0
      Exams.findAll.map { exam =>
        if (exam.cid == candidate.id.getOrElse(0)) n = exam.cid }
      listK = listK+ CandidateExam(candidate.id.getOrElse(0),Option(n),candidate.date, candidate.firstname ,candidate.lastname)
    }

    listK.map(e=> 
      Logger.info(e.toString))

    Ok(views.html.candidate.candidate(listK.toList))
  }

  
}