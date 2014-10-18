package services

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Utility for handling ISO dates
 * @author George Georgovassilis
 * 
 */
trait DateParser {

  def makeSimpleDateFormatter():SimpleDateFormat={
    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    return sdf
  }
  
  def parseDate(dateString: String): Option[Date] = {
    val sdf = makeSimpleDateFormatter()
    var result: Option[Date] = None;
    try {
      val date = sdf.parse(dateString)
      result = Some(date)
    } catch {
      case e: Exception => {
        println(dateString + " can't be parsed")
      }
    }
    result
  }
  
  def parseFeedDate(dateString: String): Option[Date] = {
    val sdf = makeSimpleDateFormatter()
    var result: Option[Date] = None
    try {
      val date = sdf.parse(dateString)
      result = Some(date)
    } catch {
      case e: Exception => {
        println(dateString + " can't be parsed")
      }
    }
    result
  }
  
  
  def toGmt(date:Date):String={
    val sdf = makeSimpleDateFormatter()
    sdf.format(date.getTime())
  }

}