package models

import slick.driver.H2Driver.simple._
import Database.threadLocalSession
import play.api.Logger
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import play.api.Play.current
/**
 * Answer domain model.
 */
case class Response(
  id: Long,
  answerId: Long,
  resp: Option[String],
  select: Option[Boolean])


object Responses extends Table[Response]("responses") {
  def id = column[Long]("id", O.PrimaryKey)
  def answerId = column[Long]("answer_id")
  def resp = column[String]("resp")
  def select = column[Boolean]("selected")

  def * = id ~ answerId ~ resp.? ~ select.? <> (Response, Response.unapply _)
  def answer = foreignKey("answer_fk", answerId, Answers)(_.id)

  val byId = createFinderBy(_.id)
  val byAnswerId = createFinderBy(_.answerId)
   /**
   * Adds the given product to the database.
   */
  def insertDB(response: Response): Int = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Responses.insert(response)
    }
  }

  /**
   * Returns a list of products from the database.
   */
  def getAll: List[Response] = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Query(Responses).list
    }
  }

  def findById(id: Long): Option[Response] = Database.forDataSource(DB.getDataSource()) withSession {
    Responses.byId(id).firstOption
  }

  def findByAnswerId(answerId: Long): Option[Response] = Database.forDataSource(DB.getDataSource()) withSession {
    Responses.byAnswerId(answerId).firstOption
  }
}
  