package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger

import scala.concurrent.duration._
/**
 * Question domain model.
 */
case class Candidate(
  id: Option [Long],
  date: String,
  firstname: String,
  lastname: String)

case class CandidateFootprint(
  firstname: String,
  lastname: String)
  
case class CandidateExam (
  id: Long,
  examid: Option[Long],
  date: String,
  firstname: String,
  lastname: String

  )


/**
 * Slick database mapping.
 */
object Candidates extends Table[Candidate]("candidates") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def date = column[String]("datecrea")
  def firstname = column[String]("firstname")
  def lastname = column[String]("lastname")


  def * = id.? ~ date ~ firstname ~ lastname<> (Candidate, Candidate.unapply _)
  def autoInc = id.?  ~ date ~ firstname ~ lastname  <> (Candidate, Candidate.unapply _) returning id

  val anonymousId =  1L;


  def forInsert =  date ~ firstname ~ lastname <> (
    t => Candidate(None, t._1, t._2, t._3),
    (p: Candidate) => Some(( p.date, p.firstname, p.lastname))
  )


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Candidate] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Candidates).sortBy(_.id).list
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
   * Inserts the given product.
   */
  import java.util._
  import java.text._
  def insert(candidate: CandidateFootprint) {

    play.api.db.slick.DB.withSession { implicit session =>
      Candidates.forInsert.insert(Candidate(None,new SimpleDateFormat("yyyy-MM-dd").format(new Date),candidate.firstname,candidate.lastname))
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