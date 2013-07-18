package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger

import scala.concurrent.duration._
import java.sql.Timestamp
import org.joda.time.DateTime
/**
 * Question domain model.
 */
case class Exam(
  id: Option [Long],
  cid: Long,
  tid: Long,
  token: Option[String],
  date: Option[String],
  notifier_1: String,
  note: Float)

case class ExamFootprint(
  notifier_1: String,
  tid: Long)

/**
 * Slick database mapping.
 */
object Exams extends Table[Exam]("exams") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cid = column[Long]("cid")
  def tid = column[Long]("tid")
  def token = column[String]("token")
  def date = column[String]("datecrea")
  def notifier_1 = column[String]("notifier")
  def note = column[Float]("note")
  

  /*implicit val dateTime: TypeMapper[DateTime]
  = MappedTypeMapper.base[DateTime, Timestamp](dt => new
      Timestamp(dt.getMillis), ts => new DateTime(ts.getTime))*/

  def * = id.? ~ cid ~ tid ~ token.? ~ date.? ~ notifier_1 ~ note <> (Exam, Exam.unapply _)
  def autoInc = id.?  ~ cid ~ tid ~ token.? ~ date.? ~ notifier_1 ~ note <> (Exam, Exam.unapply _) returning id

  def forInsert =  cid ~ tid ~ token.? ~ date.? ~ notifier_1 ~ note  <> (
    t => Exam(None, t._1, t._2, t._3, t._4, t._5,t._6),
    (p: Exam) => Some(( p.cid, p.tid, p.token, p.date, p.notifier_1, p.note)))


  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Exams.where(_.id === id).delete
    }
  }

  def find(id: Long): Exam = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).filter(_.id === id).list.head
  }



  def findAll(): List[Exam] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).sortBy(_.id).list
  }

  
  /**
   * Returns all products sorted by EAN code.
   */
  def findAllbyCId(cid: Long): Exam = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).filter(_.cid === cid).list.head
  }

  /**
   * Returns all products sorted by EAN code.
   */
  def findbyToken(token: String): Option[Exam] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).filter(_.token === token).firstOption
  }

  /*def generateToken() : String {
    return "AAAAA"
  }*/
 import java.util.UUID;
  def insert(cid: Long, exam: ExamFootprint) {
      play.api.db.slick.DB.withSession { implicit session =>

      Exams.forInsert.insert(Exam(None,cid,exam.tid,Option(UUID.randomUUID().toString()),None,exam.notifier_1,0))
    }
  }

  def updateNote( e: Exam, note : Float) {
      play.api.db.slick.DB.withSession { implicit session =>
        Exams.where(_.id === e.id).update( 
          Exam(e.id, e.cid, e.tid, e.token, e.date, e.notifier_1, note))
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, exam: Exam) {
    play.api.db.slick.DB.withSession { implicit session =>
      Exams.where(_.id === id).update(exam)
    }
  }

}

