package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class Question(
  id: Option [Long],
  tid: Long,
  description: String)


/**
 * Slick database mapping.
 */
object Questions extends Table[Question]("questions") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def tid = column[Long]("tid")
  def description = column[String]("description")
  def * = id.? ~ tid ~ description <> (Question, Question.unapply _)
  def autoInc = id.?  ~ tid ~ description  <> (Question, Question.unapply _) returning id

  def forInsert =  tid ~ description <> (
    t => Question(None, t._1, t._2),
    (p: Question) => Some(( p.tid, p.description)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Questions.ddl.dropStatements.mkString("/n"))
      Logger.info(Questions.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Questions).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Questions.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Question] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Questions).filter(_.id === id).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Question] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Questions).sortBy(_.id).list
  }

  /**
   * Returns all questions sorted by id.
   */
  def findAllbyTid(tid: Long): List[Question] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Questions).filter(_.tid === tid).sortBy(_.id).list
  }

  /**
   * Inserts the given product.
   */
  def insert(question: Question) {
    play.api.db.slick.DB.withSession { implicit session =>
      Questions.forInsert.insert(question)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: Question) {
    play.api.db.slick.DB.withSession { implicit session =>
      Questions.where(_.id === id).update(question)
    }
  }

}