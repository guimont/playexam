import play.api._

import models._

object Global extends GlobalSettings {
  
  
  
}

/**
 * Initial set of data to be imported 
 * in the sample application.
 */
object InitialData {
  
  def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)
  /*
  def insert() = {
    
    if(Questions.getAll.isEmpty) {
      
      Seq(
        /*Question(None, "JEX", "Which declaration of the main method below would allow a class to be started as a standalone program. Select the one correct answer. "),
        Question(None, "JEX", """What all gets printed when the following code is compiled and run? Select the three correct answers. public class xyz {"""


            /*public static void main(String args[]) { \n for(int i = 0; i < 2; i++) {  \n for(int j = 2; j>= 0; j--) {  \n  if(i == j) break;  \n System.out.println(\"i=\" + i + \" j=\"+j);  \n }  \n }  \n }  \n }"})*/
        */
      ).foreach(Questions.insertDB)
    
      
    }
    
  }*/
  
}