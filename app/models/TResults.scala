package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class TResult(
  id: Option[Long],
  qid: Long,
  tid: Long,
  open: Boolean,
  resp: String
  )


/**
 * Slick database mapping.
 */
object TResults extends Table[TResult]("responses") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qId")
  def tid = column[Long]("tid")
  def open = column[Boolean]("open")
  def response = column[String]("response")
  def * = id.? ~ qid ~ tid ~ open ~ response <> (TResult, TResult.unapply _)
  def autoInc = id.?  ~ qid ~ tid  ~ open ~ response  <> (TResult, TResult.unapply _) returning id

  def forInsert =  qid ~ tid ~ open ~ response <> (
    t => TResult(None, t._1, t._2 , t._3, t._4),
    (p: TResult) => Some(( p.qid, p.tid, p.open , p.resp)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(TResults.ddl.dropStatements.mkString("/n"))
      Logger.info(TResults.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(TResults).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      TResults.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[TResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(TResults).filter(_.id === id).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[TResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(TResults).sortBy(_.id).list
  }

  /**
   * Returns all products sorted by question id .
   */
  def findAllbyQId(qid: Long): List[TResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(TResults).filter(_.qid === qid).list
  }

  /**
   * Returns all Test results filter by exam id .
   */
  def findAllbyTid(tid: Long): List[TResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(TResults).filter(_.tid === tid).sortBy(_.qid).list
  }

  /**
   * Returns all products sorted by candidate id .
   */
  def findAllbyQTid(qid: Long,tid: Long): List[TResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(TResults).filter(_.tid === tid).filter(_.qid === qid).list
  }

  /**
   * Inserts the given product.
   */
  def insert(result: TResult) {
    play.api.db.slick.DB.withSession { implicit session =>
      TResults.forInsert.insert(result)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: TResult) {
    play.api.db.slick.DB.withSession { implicit session =>
      TResults.where(_.id === id).update(question)
    }
  }
}