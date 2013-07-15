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
  import models.{TResult,TResults}
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
      request.session.get("SessionID").map { Sid =>
        CorrectExam(Sid.toInt)
      }.getOrElse(Unauthorized("Oops, you are not connected"))    
      Ok(views.html.test.end()).withNewSession
    }



    def predicate(t:String,c:String) : Float = {
      var note = 0
      var nb = 0

      Logger.info(t)
      Logger.info(c)
      t.split(" ").map{ i=>  
        if (c.indexOf(i)>=0) note = note + 1
        nb = nb + 1
      }
      Logger.info(note.toString +" "+nb.toString)
      note.toFloat/nb.toFloat
    }


    def CorrectExam(id: Long) {
      val e = Exams.find(id)
      var note = e.note

      val listT = TResults.findAllbyTid(e.tid)
      val listC = CResults.findAllbyEid(id)

      listC.zipAll(listT,"",4).map { n =>
        note = note + predicate(n.t.resp, n.c.resp)
      }
      /*for {t <- listT
          c <- listC
        
      } yield predicate(t.resp, c.resp)*/

      /*CResults.findAllbyEid(id).map { c => 
        TResults.findAllbyTid(e.tid).map { t =>
          if (t.qid == c.qid && t.open == true) {
            note = note + predicate(t.resp, c.resp)
            Exams.updateNote(e,note)
          }
        }
      }*/
    }
}