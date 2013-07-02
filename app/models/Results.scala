package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class Result(
  id: Option[Long],
  Qid: Long,
  resp: String
  )


/**
 * Slick database mapping.
 */
object Results extends Table[Result]("results") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qId")
  def response = column[String]("result")
  def * = id.? ~ qid ~ response <> (Result, Result.unapply _)
  def autoInc = id.?  ~ qid ~ response  <> (Result, Result.unapply _) returning id

  def forInsert =  qid ~ response <> (
    t => Result(None, t._1, t._2),
    (p: Result) => Some(( p.Qid, p.resp)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Results.ddl.dropStatements.mkString("/n"))
      Logger.info(Results.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Results).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Results.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Result] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Results).filter(_.id === id).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Result] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Results).sortBy(_.id).list
  }

  /**
   * Returns all products sorted by EAN code.
   */
  def findAllbyQId(qid: Long): List[Result] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Results).filter(_.qid === qid).sortBy(_.id).list
  }

  /**
   * Inserts the given product.
   */
  def insert(question: Result) {
    play.api.db.slick.DB.withSession { implicit session =>
      Results.forInsert.insert(question)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: Result) {
    play.api.db.slick.DB.withSession { implicit session =>
      Results.where(_.id === id).update(question)
    }
  }


  def decode(id: Long, res:String ) {

  }
}