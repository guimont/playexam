package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger

/**
 * Candidate result object
 * @param id Candidate Result id
 * @param qid @Question id
 * @param eid @Exam id {foreign key for candidate and test object}
 * @param resp response for qid position
 * @param note result for the object follow Result object
 */
case class CResult(
  id: Option[Long],
  qid: Long,
  eid: Long,
  resp: String,
  note: Float
  )


/**
 * Slick mapping for CResult object
 */
object CResults extends Table[CResult]("results") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qId")
  def eid = column[Long]("eid")
  def response = column[String]("response")
  def note = column[Float]("note")
  def * = id.? ~ qid ~ eid ~  response ~ note <> (CResult, CResult.unapply _)
  def autoInc = id.?  ~ qid ~ eid  ~ response ~ note <> (CResult, CResult.unapply _) returning id

  def forInsert =  qid ~ eid ~ response ~ note <> (
    t => CResult(None, t._1, t._2 , t._3, t._4),
    (p: CResult) => Some(( p.qid, p.eid, p.resp, p.note)))


  /**
   * Delete Candidate result object
   * @param id
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.where(_.id === id).delete
    }
  }

  /**
   * Find Candidate result object with id
   * @param id
   * @return
   */
  def find(id: Long): Option[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.id === id).list.headOption
  }


  /**
   * Returns all Candidate result object
   */
  def findAll: List[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).sortBy(_.id).list
  }

  /**
   * * Returns all Candidate result object sorted by exam id .
   * @param eid
   * @return
   */
  def findAllbyEid(eid: Long): List[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.eid === eid).sortBy(_.qid).list
  }

  /**
   * Returns all Candidate result object sorted by exam id and question id.
   * @param qid
   * @param eid
   * @return
   */
  def findbyQEid(qid: Long,eid: Long): Option[CResult] = play.api.db.slick.DB.withSession { implicit session =>
    Query(CResults).filter(_.eid === eid).filter(_.qid === qid).firstOption
  }

  /**
   * insert result in base
   * @param result
   */
  def insert(result: CResult) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.forInsert.insert(result)
    }
  }

  /**
   * insert response in candidate test in base
   * @param qid question id
   * @param eid examn id
   * @param resp candidate response
   */
  def insertResponse(qid: Long, eid: Long , resp: String) {

    CResults.findbyQEid(qid,eid).map{c =>
      CResults.update(c.id.getOrElse(0), CResult(c.id, qid,eid, resp, 0))
    }.getOrElse(CResults.insert(CResult(None, qid,eid, resp, 0)))
  }

  /**
   * Update Candidate result
   * @param id
   * @param question
   */
  def update(id: Long, question: CResult) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.where(_.id === id).update(question)
    }
  }


  /**
   * Update Candidate result note
   * @param res
   * @param note
   */
  def updateNote(res: CResult, note: Float) {
    play.api.db.slick.DB.withSession { implicit session =>
      CResults.where(_.id === res.id).update(CResult(res.id, res.qid , res.eid ,res.resp , note ))
    }
  }


  /**
   * Decode output in candidate result format to insert in object
   * @param qid
   * @param eid
   * @param res
   */
  def decode(qid: Long, eid: Long, res:String, t:Long ) {
    var listK : String = ""
    res.split("[( ,-]").map{e=> 
      if (e.startsWith("index")) {
        listK = listK + (e.charAt(5)+16).toChar + " "
      }
    }

    CResults.findbyQEid(qid,eid).map{c =>
      CResults.update(c.id.getOrElse(0), CResult(c.id, qid,eid, listK, 0))
    }.getOrElse(CResults.insert(CResult(None, qid,eid, listK, 0)))
  }
}