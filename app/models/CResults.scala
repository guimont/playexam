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
    Query(CResults).filter(_.eid === eid).sortBy(_.qid).list
  }

  /**
   * Returns all products sorted by candidate id .
   */
  def findbyQEid(qid: Long,eid: Long): Option[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.eid === eid).filter(_.qid === qid).firstOption
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


  def decode(qid: Long, eid: Long, res:String ) {
    Logger.info(res)

    var listK : String = ""
    val delims = "[( ,-]"
    val tokens = res.split(delims)
    tokens.map{e=> 
      if (e.startsWith("index")) {
        listK = listK + (e.charAt(5)+16).toChar + " "
      }
    }

    CResults.findbyQEid(qid,eid).map{c =>
      CResults.update(c.id.getOrElse(0), CResult(c.id, qid,eid, listK))
    }.getOrElse(CResults.insert(CResult(None, qid,eid, listK)))
  }
}