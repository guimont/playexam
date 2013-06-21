package models

import slick.driver.H2Driver.simple._
import Database.threadLocalSession
import play.api.Logger
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import play.api.Play.current
/**
 * Question domain model.
 */
case class Question(
  id: Option [Long],
  name: String,
  description: String)


/**
 * Slick database mapping.
 */
object Questions extends Table[Question]("questions") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")
  def * = id.? ~ name ~ description <> (Question, Question.unapply _)
  def autoInc = id.?  ~ name ~ description  <> (Question, Question.unapply _) returning id

  val byId = createFinderBy(_.id)


  /**
   * Delete all database rows.
   * Note that an alternative would be to use slick to run DDL drop and create statements.
   */
  def reset() {
    Database.forDataSource(DB.getDataSource()) withSession {
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Questions.ddl.dropStatements.mkString("/n"))
      Logger.info(Questions.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Questions).delete
    }
  }

  /**
   * Adds the given product to the database.
   */
  def insertDB(question: Question): Long = {
    Database.forDataSource(DB.getDataSource()) withSession {
      val q = Question(None, question.name, question.description)
      Questions.autoInc.insert(q)
    }
  }

  /**
   * Returns a list of products from the database.
   */
  def getAll: List[Question] = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Query(Questions).list
    }
  }

  def findById(id: Long): Option[Question] = Database.forDataSource(DB.getDataSource()) withSession {
    Questions.byId(id).firstOption
  }
/*
  def getAllProductsWithStockItems: Map[Product, List[StockItem]] = {
  }*/
}