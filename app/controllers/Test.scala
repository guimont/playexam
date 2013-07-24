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
  import models.CResultFootprint



object Test extends Controller {

  val testForm = Form(mapping(
    "test" -> text)(CResultFootprint.apply)(CResultFootprint.unapply))

    def show(id: Long) = Action { implicit request =>
      request.session.get("SessionID").map { Sid => 
        Questions.find(id).map { question =>
          showCheck(id,Sid.toLong,question)
          
        }.getOrElse(Unauthorized("Oops, you are not connected"))
      }.getOrElse(Unauthorized("Oops, you are not connected"))
    }

    def showCheck(id:Long, sid: Long, question: Question) = {
      Ok(views.html.details(id,testForm,Tests.find(Exams.find(sid).tid).nb_q,
            question,Parts.findAllbyQId(id), FillAnswer(id,sid,Answers.findAllbyQId(id))))
    }

  def FillAnswer(id: Long, eid: Long, list:List[Answer]) :List[Answer]  =  { 
    var listA : Set[Answer] = Set()
    CResults.findbyQEid(id,eid).map { res =>
      var checked  = new Array[Boolean](22)
      res.resp.split(" ").map { i =>
        Logger.info(i+" "+(i.charAt(0)-64).toString)
        checked((i.charAt(0)-64)) = true
      }
      for ((a,index) <-list.zipWithIndex) {
        Logger.info(a.toString + " " +index)
        listA = listA + Answer(a.id, a.Qid, a.resp, checked(index+1))
      }
    }.getOrElse{
      list.map (l=> listA = listA + Answer(l.id, l.Qid, l.resp, l.check))
    }
    listA.toList.sortBy(l =>l.id)
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

     def answerText(id: Long) = Action { implicit request =>
        Logger.info(request.body.toString); 
        request.session.get("SessionID").map { Sid =>
        
        
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
      //SendMail 
      Ok(views.html.test.end()).withNewSession
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
         Logger.info(i)
        if (c.indexOf(i)>=0) note = note + 1
        nb = nb + 1
      }
      Logger.info(note.toFloat + " / " + nb.toFloat +" = " + note.toFloat/nb.toFloat)
      note.toFloat/nb.toFloat
    }


    def CorrectExam(id: Long) {
      val e = Exams.find(id)
      var note = e.note

      CResults.findAllbyEid(id).zip(TResults.findAllbyTid(e.tid)).map { n =>
        if(n._2.open == true) {
          note = note + predicate(n._1.resp, n._2.resp)
          Exams.updateNote(e,note)
        }
      }
    }
}