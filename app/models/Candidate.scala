package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger

import scala.concurrent.duration._
import java.util._
import java.sql.Timestamp
import org.joda.time.DateTime
/**
 * Question domain model.
 */
case class Candidate(
  id: Option [Long],
  examId: Option [Long],
  /*date: DateTime,*/
  date: String,
  firstname: String,
  lastname: String)


/**
 * Slick database mapping.
 */
object Candidates extends Table[Candidate]("candidates") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def examId = column[Long]("examId")
  /*def date = column[DateTime]("datecrea")*/
  def date = column[String]("datecrea")
  def firstname = column[String]("firstname")
  def lastname = column[String]("lastname")


  implicit val dateTime: TypeMapper[DateTime]
  = MappedTypeMapper.base[DateTime, Timestamp](dt => new
      Timestamp(dt.getMillis), ts => new DateTime(ts.getTime))

  def * = id.? ~ examId.? ~ date ~ firstname ~ lastname<> (Candidate, Candidate.unapply _)
  def autoInc = id.?  ~ examId.? ~ date ~ firstname ~ lastname  <> (Candidate, Candidate.unapply _) returning id

  def forInsert =  examId.? ~ date ~ firstname ~ lastname <> (
    t => Candidate(None, t._1, t._2, t._3, t._4),
    (p: Candidate) => Some(( p.examId, p.date, p.firstname, p.lastname)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Candidates.ddl.dropStatements.mkString("/n"))
      Logger.info(Candidates.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Candidates).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Candidates.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Candidate] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Candidates).filter(_.id === id).list.headOption
  }


  
  /**
   * Returns all products sorted by EAN code.
   */
  def findAllbyexamId(examId: Long): Option[Candidate] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Candidates).filter(_.examId === examId).list.headOption
  }

  /**
   * Inserts the given product.
   */
  def insert(question: Candidate) {
    play.api.db.slick.DB.withSession { implicit session =>
      Candidates.forInsert.insert(question)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: Candidate) {
    play.api.db.slick.DB.withSession { implicit session =>
      Candidates.where(_.id === id).update(question)
    }
  }

}