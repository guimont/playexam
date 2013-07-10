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
  import models.{Candidate,Candidates}
  import models.StartFootprint

  case class Index(name:String,res:List[Boolean])

  object Application extends Controller {
    
     /*def index = Action { request =>
      request.session.get("SessionID").map { Sid =>
      Ok(views.html.index(Questions.findAll))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
      
    }*/

   

  	def show(id: Long) = Action { implicit session =>
        Questions.find(id).map { question =>

          Ok(views.html.details(id,question,Parts.findAllbyQId(id), Answers.findAllbyQId(id)))
        }.getOrElse(NotFound)
      
    }


    def answer(id: Long) = Action { implicit request =>
     
      Logger.info(request.body.toString); 
      request.session.get("SessionID").map { Sid =>
        CResults.decode(id,Sid.toInt,request.body.toString)
      }

      var next : Long = 0
      if (id>=3) next = 1
      else next = id+1
      Redirect(routes.Application.show(next))
    }


    val startFootprint = Form(mapping(
    "startid" -> longNumber)(StartFootprint.apply)(StartFootprint.unapply))
    
    def startForm = Action {
      Ok(views.html.index(startFootprint))
    }


    

    def start = Action {
      Redirect(routes.Application.show(1))/*.withSession(
      "SessionID" -> id.toString
    )*/

    }

  }