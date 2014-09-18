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
  qid: Long,
  tid: Long,
  description: String,
  open: Boolean)


/**
 * Slick database mapping.
 */
object Questions extends Table[Question]("questions") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qid")
  def tid = column[Long]("tid")
  def description = column[String]("description")
  def open = column[Boolean]("open")
  def * = id.? ~ qid  ~ tid ~ description ~ open <> (Question, Question.unapply _)
  def autoInc = id.?  ~ qid ~ tid ~ description ~ open <> (Question, Question.unapply _) returning id

  def forInsert =  qid ~tid ~ description ~ open <> (
    t => Question(None, t._1, t._2, t._3, t._4),
    (p: Question) => Some(( p.qid, p.tid, p.description, p.open)))



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


  def findQ(qid: Long,tid: Long): Option[Question] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Questions).filter(_.qid === qid).filter(_.tid === tid).list.headOption
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