package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class CResult(
  id: Option[Long],
  qid: Long,
  eid: Long,
  resp: String
  )


/**
 * Slick database mapping.
 */
object CResults extends Table[CResult]("results") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qId")
  def eid = column[Long]("eid")
  def response = column[String]("response")
  def * = id.? ~ qid ~ eid ~  response <> (CResult, CResult.unapply _)
  def autoInc = id.?  ~ qid ~ eid  ~ response  <> (CResult, CResult.unapply _) returning id

  def forInsert =  qid ~ eid ~ response <> (
    t => CResult(None, t._1, t._2 , t._3),
    (p: CResult) => Some(( p.qid, p.eid, p.resp)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(CResults.ddl.dropStatements.mkString("/n"))
      Logger.info(CResults.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(CResults).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.id === id).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).sortBy(_.id).list
  }

  /**
   * Returns all products sorted by question id .
   */
  def findAllbyQId(qid: Long): List[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.qid === qid).list
  }

  /**
   * Returns all products sorted by candidate id .
   */
  def findAllbyEid(eid: Long): List[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.eid === eid).list
  }

  /**
   * Returns all products sorted by candidate id .
   */
  def findAllbyQEid(qid: Long,eid: Long): List[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.eid === eid).filter(_.qid === qid).list
  }

  /**
   * Inserts the given product.
   */
  def insert(result: CResult) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.forInsert.insert(result)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: CResult) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.where(_.id === id).update(question)
    }
  }


  def decode(eid: Long,qid: Long, res:String ) {
    val result = CResult(None, qid,eid, res)
    CResults.insert(result)
  }
}