  package controllers

  import play.api._
  import play.api.mvc._
  import play.api.data._
  import play.api.data.Forms._
  import play.api.mvc.RequestHeader

  import models._
  import play.api.cache._
  import play.api.Play.current
  import java.util.UUID
  import models.StartFootprint


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
    def index = Action { implicit request =>

      val e = Exam(None,Candidates.anonymousId, 1L,Option(UUID.randomUUID().toString()),None,"",0)
      Exams.insert(e)

      Ok(views.html.start(startFootprint.fill(StartFootprint(e.token.getOrElse("")))))
    }

    /**
     * Login page.
     */
    def login = Action { implicit request =>
      //Ok(views.html.login(loginForm))
      Redirect(routes.CandidateForm.candidates).withSession("email" -> "admin.orsyp.com")
    }

    /**
     * Handle login form submission.
     */
    def authenticate = Action { implicit request =>
      Redirect(routes.CandidateForm.candidates).withSession("email" -> "admin.orsyp.com")
      /*loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login(formWithErrors)),
        user => Redirect(routes.CandidateForm.candidates).withSession("email" -> user._1)
      )*/
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
      Ok(views.html.start(startFootprint))
    }

    def startFormToken(token : String) = Action {
      Ok(views.html.start(startFootprint.fill(StartFootprint(token))))
    }

    import java.util._
    import java.text._
    def start = Action { implicit request =>
      startFootprint.bindFromRequest.fold(
      formWithErrors => {
        Logger.info(formWithErrors.toString)
        Ok(views.html.start(formWithErrors))},
      success = { token =>
          Logger.info(token.startid); 
          Exams.findbyToken(token.startid).map {
            exam => {
              val sid = exam.id.getOrElse(0).toString
              
              Logger.info("add in cache : "+ sid)
              Cache.set(sid, System.currentTimeMillis())
              Redirect(routes.Test.show(1)).withSession(
              "SessionID" -> sid)
            }
          } getOrElse Ok(views.html.start(startFootprint))
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