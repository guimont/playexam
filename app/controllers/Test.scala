package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.{Question,Questions}
import models.{Part,Parts}
import models.{Answer,Answers}
import models.{CResult,CResults}
import models.{TResult,TResults}
import models.Exams
import models.Tests
import models.CResultFootprint
import play.api.cache.Cache
import play.api.Play.current
import scala.collection.immutable.List
import scala.List
import scala.collection.mutable.ListBuffer


object Test extends Controller {

  val testForm = Form(mapping(
    "test" -> text)(CResultFootprint.apply)(CResultFootprint.unapply))

  def show(id: Long) = Action { implicit request =>
    request.session.get("SessionID").map { Sid =>
      Cache.set(Sid+id, System.currentTimeMillis())
      val e = Exams.find(Sid.toLong)
      Questions.findQ(id,e.tid).map { question =>
        if (!question.open) 
          Ok(views.html.test.details(id,Tests.find(e.tid).nb_q,
            question,Parts.findAllbyQId(question.id.getOrElse(0)), Answers.fillAnswerCheck(id,Sid.toLong,Answers.findAllbyQId(question.id.getOrElse(0))),
            WebSockets.getTimer(request)))
        else 
          Ok(views.html.test.text(id,testForm, Tests.find(Exams.find(Sid.toLong).tid).nb_q,
            question, Parts.findAllbyQId(question.id.getOrElse(0)), Answers.findbyQId(id),WebSockets.getTimer(request)))
          
      }.getOrElse(Unauthorized("Oops, you are not connected"))
    }.getOrElse(Unauthorized("Oops, you are not connected"))
  }

  /**
   * answer request
   * @param id : question id
   * @return next page
   */
  def answer(id: Long) = Action { implicit request =>
    request.session.get("SessionID").map { Sid =>
      /**ref date for calculate time answer*/
      val tCache:Long = Cache.getAs[Long](Sid+id).getOrElse(0);
      val t = (System.currentTimeMillis()-tCache)/1000;
      Cache.remove(Sid+id);
      CResults.decode(id,Sid.toInt,request.body.toString, t)

      Cache.set(Sid+"L",Cache.getAs[ListBuffer[Long]](Sid+"L")
        .getOrElse(new ListBuffer[Long]())+=t)
        
      if (Tests.find(Exams.find(Sid.toLong).tid).nb_q == id)
        Redirect(routes.Test.end)
      else
        Redirect(routes.Test.show(id+1))
    }.getOrElse(Unauthorized("Oops, you are not connected"))      
  }

  def answerText(id: Long) = Action { implicit request =>
    request.session.get("SessionID").map { Sid =>

      testForm.bindFromRequest.fold(  
        formWithErrors => {
          Redirect(routes.Test.show(id))},
        success = { test =>
          CResults.insertResponse(id,Sid.toInt,test.test)
          if (Tests.find(Exams.find(Sid.toLong).tid).nb_q == id)
            Redirect(routes.Test.end)
          else
            Redirect(routes.Test.show(id+1))
        }
      )
    }.getOrElse(Unauthorized("Oops, you are not connected"))   
  }


  def end = Action { implicit request =>
    request.session.get("SessionID").map { Sid =>
      val result = CorrectExam(Sid.toInt)
      Ok(views.html.test.end(result)).withNewSession
    }.getOrElse(Unauthorized("Oops, you are not connected"))   
      //SendMail
  }


  /*val editForm = Form(mapping(
    "resp"  -> text,
    "note" -> Float
  ) (CResult.apply)(CResult.unapply)
*/

  def edit(id: Long) = Action  { implicit request =>
    request.session.get("SessionID").map { Sid =>
      Questions.findQ(id,Sid.toLong).map { question =>
        Ok(views.html.test.edit(id,
            question, Answers.fillAnswerCheck(id,Sid.toLong,Answers.findAllbyQId(id))))
      }.getOrElse(Unauthorized("Oops, you are not connected"))
    }.getOrElse(Unauthorized("Oops, you are not connected"))
  }

  def correctManual(id: Long) = Action { implicit request =>

    Redirect(routes.Test.edit(1)).withSession(
              "SessionID" -> id.toString)
  }


  def SendMail = {
    import com.typesafe.plugin._
    import play.api.Play.current
    val mail = use[MailerPlugin].email
    mail.setSubject("mailer")
    mail.addRecipient("gmo@orsyp.com")
    mail.addFrom("gmo@orsyp.com")
      //sends both text and html
    mail.send( "text", "<html>html</html>")
  }

  def predicate(c:String,t:String) : Float = {
    var note = 0
    var nb = 0
 
    t.split(" ").map{ i=> 
      if (c.indexOf(i)>=0) note = note + 1
      nb = nb + 1
    } 
    note.toFloat/nb.toFloat
  }


  def CorrectExam(id: Long): Float = {
    val e = Exams.find(id)
    var note = e.note

    CResults.findAllbyEid(id).zip(TResults.findAllbyTid(e.tid)).map { n =>
      if(n._2.open == true) {
        val noteQ = predicate(n._1.resp, n._2.resp)
        CResults.updateNote(n._1,noteQ)
        note = note + noteQ
      }
    }
    Exams.updateNote(e,note)
    note
  }
}