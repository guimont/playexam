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
case class TestResponse(
  id: Option[Long],
  answerId: Long,
  resp: String)


object TestResponses extends Table[TestResponse]("responses") {
  def id = column[Long]("responseId", O.PrimaryKey, O.AutoInc)
  def answerId = column[Long]("answerId")
  def resp = column[String]("response")

  def * = id.? ~ answerId ~ resp  <> (TestResponse, TestResponse.unapply _)
  def autoInc = id.?  ~ answerId ~ resp  <> (TestResponse, TestResponse.unapply _) returning id
  //def answer = foreignKey("answer_fk", answerId, Answers)(_.id)

  val byId = createFinderBy(_.id)
  val byAnswerId = createFinderBy(_.answerId)


  def reset() {
    Database.forDataSource(DB.getDataSource()) withSession {
      // Output database DDL create statements to bootstrap Evolutions file.
      Logger.info(TestResponses.ddl.dropStatements.mkString("/n"))
      Logger.info(TestResponses.ddl.createStatements.mkString("/n"))

      // Delete all rows
      Query(TestResponses).delete
    }
  }

   /**
   * Adds the given product to the database.
   */
  def insertDB(response: TestResponse): Long = {
    Database.forDataSource(DB.getDataSource()) withSession {
      val r = TestResponse(None, response.answerId, response.resp)
      TestResponses.autoInc.insert(r)
    }
  }

  /**
   * Returns a list of products from the database.
   */
  def getAll: List[TestResponse] = {
    Database.forDataSource(DB.getDataSource()) withSession {
      Query(TestResponses).list
    }
  }

  def findById(id: Long): Option[TestResponse] = Database.forDataSource(DB.getDataSource()) withSession {
    TestResponses.byId(id).firstOption
  }

  def findByAnswerId(answerId: Long): Seq[TestResponse] = Database.forDataSource(DB.getDataSource()) withSession {
    TestResponses.byAnswerId(answerId).list
  }
}
  