import play.api._

import models._

object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    InitialData.insert()
  }
  
}

/**
 * Initial set of data to be imported 
 * in the sample application.
 */
object InitialData {
  
  def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)
  
  def insert() = {
    
    if(Questions.getAll.isEmpty) {
      
      Seq(
        Question(None, "Exo", "jave c'est cool ou pas")
        
      ).foreach(Questions.insertDB)
    
      
    }
    
  }
  
}