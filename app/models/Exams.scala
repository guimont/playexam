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
  date: String,
  notifier_1: String,
  note: Int)

case class ExamFootprint(
  notifier_1: String)

/**
 * Slick database mapping.
 */
object Exams extends Table[Exam]("exams") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def cid = column[Long]("cid")
  def date = column[String]("datecrea")
  def notifier_1 = column[String]("notifier")
  def note = column[Int]("note")
  

  /*implicit val dateTime: TypeMapper[DateTime]
  = MappedTypeMapper.base[DateTime, Timestamp](dt => new
      Timestamp(dt.getMillis), ts => new DateTime(ts.getTime))*/

  def * = id.? ~ cid ~ date ~ notifier_1 ~ note <> (Exam, Exam.unapply _)
  def autoInc = id.?  ~ cid ~ date ~ notifier_1 ~ note <> (Exam, Exam.unapply _) returning id

  def forInsert =  cid ~ date ~ notifier_1 ~ note  <> (
    t => Exam(None, t._1, t._2, t._3, t._4),
    (p: Exam) => Some(( p.cid, p.date, p.notifier_1, p.note)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Exams.ddl.dropStatements.mkString("/n"))
      Logger.info(Exams.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Exams).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Exams.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Exam] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).filter(_.id === id).list.headOption
  }



  def findAll(): List[Exam] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).sortBy(_.id).list
  }

  
  /**
   * Returns all products sorted by EAN code.
   */
  def findAllbyCId(cid: Long): Option[Exam] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Exams).filter(_.cid === cid).list.headOption
  }

 
   def insert(cid: Long, exam: ExamFootprint) {
    play.api.db.slick.DB.withSession { implicit session =>
      Exams.forInsert.insert(Exam(None,cid,"2012",exam.notifier_1,0))
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: Exam) {
    play.api.db.slick.DB.withSession { implicit session =>
      Exams.where(_.id === id).update(question)
    }
  }

}