  package controllers

  import play.api._
  import play.api.mvc._
  import play.api.data._
  import play.api.data.Forms._
  import play.api.mvc.RequestHeader



  import models.{Candidate,Candidates}
  import models.StartFootprint
  import models.Exams

  case class Index(name:String,res:List[Boolean])

  object Application extends Controller {
    
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
              Redirect(routes.Test.show(1)).withSession(
              "SessionID" -> exam.id.getOrElse(0).toString)
            }
          } getOrElse Ok(views.html.index(startFootprint))
        }
      )
    }

}