package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.Logger
/**
 * Question domain model.
 */
case class Test(
  id: Option [Long],
  name: String,
  nb_q: Long,
  delay: Long)

case class TestName(id: Long, name: String)

/**
 * Slick database mapping.
 */
object Tests extends Table[Test]("tests") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def nb_q = column[Long]("nb_q")
  def delay = column[Long]("delay")
 
  def * = id.? ~ name ~ nb_q ~ delay <> (Test, Test.unapply _)
  def autoInc = id.?  ~ name ~ nb_q ~ delay  <> (Test, Test.unapply _) returning id

  def forInsert =  name ~ nb_q ~ delay <> (
    t => Test(None, t._1, t._2, t._3),
    (p: Test) => Some(( p.name, p.nb_q, p.delay)))


  /**
   * Deletes a product.
   */
  def delete(id: Long) {
    play.api.db.slick.DB.withSession { implicit session =>
      Tests.where(_.id === id).delete
    }
  }

  def find(id: Long): Test = play.api.db.slick.DB.withSession { implicit session =>
    Query(Tests).filter(_.id === id).list.head
  }


  /**
   * Returns all products sorted by EAN code.
   */
  def findAll: List[Test] = play.api.db.slick.DB.withSession { implicit session =>
    Query(Tests).sortBy(_.id).list
  }

  /**
   * Inserts the given product.
   */
  def insert(test: Test) {
    play.api.db.slick.DB.withSession { implicit session =>
      Tests.forInsert.insert(test)
    }
  }

  /**
   * Updates the given product.
   */
  def update(id: Long, test: Test) {
    play.api.db.slick.DB.withSession { implicit session =>
      Tests.where(_.id === id).update(test)
    }
  }

}


object TestName {
  
  /**
   * Construct the Map[String,String] needed to fill a select options set.
   */
  def options: Seq[(String,String)] = { 
    Tests.findAll.map(c => c.id.getOrElse(0).toString -> c.name)
  }
  
}