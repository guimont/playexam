package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**/



case class User(email: String, name: String, password: String)

object Users extends Table[User]("users") {

  def email = column[String]("email", O.PrimaryKey)
  def name = column[String]("name")
  def password = column[String]("password")
 
  def * = email ~ name ~ password <> (User, User.unapply _)
 
   def findByEmail(email: String): Option[User] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Users).filter(_.email === email).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[User] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Users).list
  }


  /**
   * Inserts the given product.
   */
  def insert(user: User) {
    play.api.db.slick.DB.withSession { implicit session =>
      Users.insert(user)
    }
  }
      
  def authenticate(email: String, password: String): Option[User] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Users).filter(_.email === email).list.headOption
  }
  
  /*
   * Authenticate a User.
   *
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select * from user where 
         email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.simple.singleOpt)
    }*
  }*/
   
}
