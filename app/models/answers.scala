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
  
  def * = id.? ~ qid ~ response ~false <> (Answer, Answer.unapply _)
  def autoInc = id.?  ~ qid ~ response ~false  <> (Answer, Answer.unapply _) returning id

  def forInsert =  qid ~ response  <> (
    t => Answer(None, t._1, t._2,false),
    (p: Answer) => Some(( p.Qid, p.resp)))



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
   * Returns Answer for question with id
   */
  def findbyQId(qid: Long): Answer = play.api.db.slick.DB.withSession { implicit session =>
    Query(Answers).filter(_.qid === qid).sortBy(_.id).list.head
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


  def fillAnswerCheck(id: Long, eid: Long, list:List[Answer]) :List[Answer]  =  { 
    var listA : Set[Answer] = Set()
    CResults.findbyQEid(id,eid).map { res =>
      var checked  = new Array[Boolean](22)
      res.resp.split(" ").map { i =>
        checked((i.charAt(0)-64)) = true
      }
      for ((a,index) <-list.zipWithIndex) {
        listA = listA + Answer(a.id, a.Qid, a.resp, checked(index+1))
      }
    }.getOrElse{
      list.map (l=> listA = listA + Answer(l.id, l.Qid, l.resp, l.check))
    }
    listA.toList.sortBy(l =>l.id)
  }

}