package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class Answer(
  id: Option[Long],
  Qid: Long,
  resp: String,
  check: Boolean)


/**
 * Slick database mapping.
 */
object Answers extends Table[Answer]("answers") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qId")
  def response = column[String]("answer")
  def check = column[Boolean]("check")
  def * = id.? ~ qid ~ response ~ check <> (Answer, Answer.unapply _)
  def autoInc = id.?  ~ qid ~ response ~ check  <> (Answer, Answer.unapply _) returning id

  def forInsert =  qid ~ response ~ check <> (
    t => Answer(None, t._1, t._2, t._3),
    (p: Answer) => Some(( p.Qid, p.resp, p.check)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Answers.ddl.dropStatements.mkString("/n"))
      Logger.info(Answers.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Answers).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Answers.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Answer] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Answers).filter(_.id === id).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Answer] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Answers).sortBy(_.id).list
  }

  /**
   * Returns all products sorted by EAN code.
   */
  def findAllbyQId(qid: Long): List[Answer] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Answers).filter(_.qid === qid).sortBy(_.id).list
  }

  /**
   * Inserts the given product.
   */
  def insert(question: Answer) {
    play.api.db.slick.DB.withSession { implicit session =>
      Answers.forInsert.insert(question)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: Answer) {
    play.api.db.slick.DB.withSession { implicit session =>
      Answers.where(_.id === id).update(question)
    }
  }

}