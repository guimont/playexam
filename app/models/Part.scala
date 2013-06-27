package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class Part(
  id: Option [Long],
  Qid: Long,
  part: String)


/**
 * Slick database mapping.
 */
object Parts extends Table[Part]("parts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def qid = column[Long]("qId")
  def part = column[String]("part")
  def * = id.? ~ qid ~ part <> (Part, Part.unapply _)
  def autoInc = id.?  ~ qid ~ part  <> (Part, Part.unapply _) returning id

  def forInsert =  qid ~ part <> (
    t => Part(None, t._1, t._2),
    (p: Part) => Some(( p.Qid, p.part)))


  def reset() {
    play.api.db.slick.DB.withSession { implicit session =>
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Parts.ddl.dropStatements.mkString("/n"))
      Logger.info(Parts.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Parts).delete
    }
  }

  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Parts.where(_.id === id).delete
    }
  }

  def find(id: Long): Option[Part] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Parts).filter(_.id === id).list.headOption
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Part] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Parts).sortBy(_.id).list
  }

  /**
   * Returns all products sorted by EAN code.
   */
  def findAllbyQId(qid: Long): List[Part] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Parts).filter(_.qid === qid).sortBy(_.id).list
  }

  /**
   * Inserts the given product.
   */
  def insert(question: Part) {
    play.api.db.slick.DB.withSession { implicit session =>
      Parts.forInsert.insert(question)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, question: Part) {
    play.api.db.slick.DB.withSession { implicit session =>
      Parts.where(_.id === id).update(question)
    }
  }

}