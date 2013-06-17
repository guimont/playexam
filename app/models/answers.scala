package models

import slick.driver.H2Driver.simple._
import Database.threadLocalSession
import play.api.Logger
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import play.api.Play.current
/**
 * Answer domain model.
 */
case class Answer(
  id: Long,
  name: String,
  description: String)


/**
 * Slick database mapping.
 */
object Answers extends Table[Answer]("answers") {
  def id = column[Long]("id", O.PrimaryKey)
  def name = column[String]("name")
  def description = column[String]("description")
  def * = id ~ name ~ description <> (Answer, Answer.unapply _)

  val byId = createFinderBy(_.id)


  /**
   * Delete all database rows.
   * Note that an alternative would be to use slick to run DDL drop and create statements.
   */
  def reset() {
    Database.forDataSource(DB.getDataSource()) withSession {
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(Answers.ddl.dropStatements.mkString("/n"))
      Logger.info(Answers.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(Answers).delete
    }
  }

  /**
   * Adds the given product to the database.
   */
  def insertDB(answer: Answer): Int = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Answers.insert(answer)
    }
  }

  /**
   * Returns a list of products from the database.
   */
  def getAll: List[Answer] = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Query(Answers).list
    }
  }

  def findById(id: Long): Option[Answer] = Database.forDataSource(DB.getDataSource()) withSession {
    Answers.byId(id).firstOption
  }
/*
  def getAllProductsWithStockItems: Map[Product, List[StockItem]] = {
  }*/
}