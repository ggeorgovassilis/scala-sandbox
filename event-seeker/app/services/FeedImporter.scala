package services

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import play.api.libs.ws._
import scala.concurrent.Future
import play.api.Play.current
import model.Event
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.Reads
import play.api.libs.json._
import play.api.libs.functional.syntax._
import model.EventTypes

/**
 * Reads events from a remote JSON feed
 * @author George Georgovassilis
 */
object FeedImporter extends DateParser {

  /**
   * Read concerts from a JSON URL
   */
  def getConcertsFrom(url: String): List[Event] = {
    implicit val context = scala.concurrent.ExecutionContext.Implicits.global
    val holder = WS.url(url)
    val delay = Duration(5, "sec")
    implicit val reader: Reads[(String, Array[String], String, Array[String], String, String, Long)] = (
      (__ \ "title").read[String] and
      (__ \ "artists").read[Array[String]] and
      (__ \ "city").read[String] and
      (__ \ "genres").read[Array[String]] and
      (__ \ "start").read[String] and
      (__ \ "end").read[String] and
      (__ \ "price").read[Long]).tupled

    val json = Await.result(holder.get, delay).json;
    val concerts = (json).as[List[(String, Array[String], String, Array[String], String, String, Long)]]
    val events: List[Event] = concerts.map(c => (
      new Event(0, EventTypes.Music, c._1, c._2, c._3, c._4, parseFeedDate(c._5).get, parseFeedDate(c._6).get, c._7)))
    events
  }

  /**
   * Read conferences from a remote URL
   */
  def getConferencesFrom(url: String): List[Event] = {
    implicit val context = scala.concurrent.ExecutionContext.Implicits.global
    val holder = WS.url(url)
    val delay = Duration(5, "sec")
    implicit val reader: Reads[(String, Array[String], String, Array[String], String, String, Long)] = (
      (__ \ "title").read[String] and
      (__ \ "speakers").read[Array[String]] and
      (__ \ "city").read[String] and
      (__ \ "topics").read[Array[String]] and
      (__ \ "start").read[String] and
      (__ \ "end").read[String] and
      (__ \ "price").read[Long]).tupled

    val json = Await.result(holder.get, delay).json;
    val concerts = (json).as[List[(String, Array[String], String, Array[String], String, String, Long)]]
    val events: List[Event] = concerts.map(c => (
      new Event(0, EventTypes.Conference , c._1, c._2, c._3, c._4, parseFeedDate(c._5).get, parseFeedDate(c._6).get, c._7)))
    events
  }

  /**
   * Read sport events from a URL
   */
  def getSportsFrom(url: String): List[Event] = {
    implicit val context = scala.concurrent.ExecutionContext.Implicits.global
    val holder = WS.url(url)
    val delay = Duration(5, "sec")
    implicit val reader: Reads[(String, Array[String], String, String, String, String, Long)] = (
      (__ \ "title").read[String] and
      (__ \ "teams").read[Array[String]] and
      (__ \ "city").read[String] and
      (__ \ "sport").read[String] and
      (__ \ "start").read[String] and
      (__ \ "end").read[String] and
      (__ \ "price").read[Long]).tupled

    val json = Await.result(holder.get, delay).json;
    val concerts = (json).as[List[(String, Array[String], String, String, String, String, Long)]]
    val events: List[Event] = concerts.map(c => (
      new Event(0, EventTypes.Sports, c._1, c._2, c._3, Array(c._4), parseFeedDate(c._5).get, parseFeedDate(c._6).get, c._7)))
    events
  }

}