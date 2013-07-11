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
  import models.Exams
  import models.Tests

  case class Index(name:String,res:List[Boolean])

  object Application extends Controller {
    
     /*def index = Action { request =>
      request.session.get("SessionID").map { Sid =>
      Ok(views.html.index(Questions.findAll))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
      
    }*/

   

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
        
        
        Redirect(routes.Application.show(id+1))

      }.getOrElse(Unauthorized("Oops, you are not connected"))

      

      
    }


    val startFootprint = Form(mapping(
    "startid" -> nonEmptyText)(StartFootprint.apply)(StartFootprint.unapply))
    
    def startForm = Action {
      Ok(views.html.index(startFootprint))
    }

    def startFormToken(token : String) = Action {
      
      Ok(views.html.index(startFootprint.fill(StartFootprint(token))))
    }


    def start = Action { implicit request =>
       startFootprint.bindFromRequest.fold(
      formWithErrors => {
        Logger.info(formWithErrors.toString)
        Ok(views.html.index(formWithErrors))},
      success = { token =>
          Logger.info(token.startid); 
          Exams.findbyToken(token.startid).map {
            exam => {
              Redirect(routes.Application.show(1)).withSession(
              "SessionID" -> exam.id.getOrElse(0).toString)
            }
          } getOrElse Ok(views.html.index(startFootprint))
        }
      )
    }

    

  }