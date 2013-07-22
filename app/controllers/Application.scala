  package controllers

  import play.api._
  import play.api.mvc._
  import play.api.data._
  import play.api.data.Forms._
  import play.api.mvc.RequestHeader



  import models.{Candidate,Candidates}
  import models.StartFootprint
  import models.Exams
  import models.Users

  case class Index(name:String,res:List[Boolean])

  object Application extends Controller {

    val loginForm = Form(
      tuple(
        "email" -> text,
        "password" -> text
      ) verifying ("Invalid email or password", result => result match {
        case (email, password) => Users.authenticate(email, password).isDefined
      })
    )

    /**
     * Login page.
     */
    def login = Action { implicit request =>
      Ok(views.html.login(loginForm))
    }

    /**
     * Handle login form submission.
     */
    def authenticate = Action { implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login(formWithErrors)),
        user => Redirect(routes.CandidateForm.candidates).withSession("email" -> user._1)
      )
    }

    /**
     * Logout and clean the session.
     */
    def logout = Action {
      Redirect(routes.Application.login).withNewSession.flashing(
        "success" -> "You've been logged out"
      )
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
              Redirect(routes.Test.show(1)).withSession(
              "SessionID" -> exam.id.getOrElse(0).toString)
            }
          } getOrElse Ok(views.html.index(startFootprint))
        }
      )
    }

}



/**
 * Provide security features
 */
trait Secured {
  
  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  // --
  
  /** 
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }
}