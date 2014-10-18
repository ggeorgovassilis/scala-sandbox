package services
import anorm._
import play.api.db.DB
import play.api.Play.current

import model._
import anorm.SqlParser.{ str, int }
import java.util.Date
import play.api.libs.json._

/**
 * Handles event categories and interests
 * @author George Georgovassilis
 * 
 */
object CategoryService {

  /**
   * Creates database tables for the categories and deletes old tables
   */
  def createTables() {

    DB.withConnection { implicit c =>
      try {
        SQL("drop table Category_Events").executeUpdate()
      } catch {
        case e: Exception => {}
      }
      createTablesIfDontExist()
    }
  }

  /**
   * Create database tables only if they don't already exist
   */
  def createTablesIfDontExist() {
    DB.withConnection { implicit c =>
      try {
        SQL("create table Category_Event(category varchar(40), event_id long, event_type varchar(20))").executeUpdate
      } catch {
        case e: Exception => {}
      }
    }
  }

  /**
   * Persist the relation between an event, its type and categories
   */
  def save(eventId: Long, eventType: EventTypes.EventType, categories: List[String]) {
    DB.withConnection { implicit c =>
      categories.foreach(category => {
        //TODO: aggregate deletes with an IN (..) clause
        SQL("delete from Category_Event where event_id = {event_id} and category = {category} and event_type = {event_type}")
          .on('event_id -> eventId)
          .on('category -> category.toLowerCase())
          .on('event_type -> eventType.toString())
          .execute
        SQL("insert into Category_Event(event_id, category, event_type) values({event_id}, {category}, {event_type})")
          .on('event_id -> eventId)
          .on('event_type -> eventType.toString())
          .on('category -> category).execute
      })
    }
  }

  /**
   * Find events that match at least one of the provided type-category combinations
   */
  def findEventsForInterests(interests: List[Interest]): List[Long] = {
    DB.withConnection { implicit c =>
      // should do a join here instead of getting IDs and reading events one by one
      var sql = "select distinct event_id from Category_Event where 0=1 "
      interests.foreach(i => sql = sql + "OR (event_type = '" + i.eventType.toString() + "' AND category = '" + i.category + "')")
      sql += " ORDER BY event_id"
      val select = SQL(sql)
      select().map(row => row[Long]("event_id")).toList
    }
  }

  /**
   * Update interest for an event
   */
  def updateCategoriesForEvent(event: Event) {
    DB.withConnection { implicit c =>
      SQL("delete from Category_Event where event_id = {event_id}").on('event_id -> event.id).executeUpdate()
      event.categories.foreach(category =>
        SQL("insert into Category_Event(event_id, category, event_type) values({event_id}, {category}, {event_type})")
          .on('event_id -> event.id)
          .on('category -> category.toLowerCase())
          .on('event_type -> event.eventType.toString())
          .execute)
    }
  }
  
  def deleteCategoriesForEventType(eventType:EventTypes.EventType){
    DB.withConnection { implicit c =>
      SQL("delete from Category_Event where event_type = {event_type}").on('event_type -> eventType.toString()).executeUpdate()
    }
  }

  def updateCategoriesForEvents(events: List[Event]) {
    createTablesIfDontExist
    events.foreach(event => updateCategoriesForEvent(event))
  }

  def getAllInterest():List[Interest]={
    DB.withConnection { implicit c =>
      var sql = "select event_type, category from Category_Event"
      val select = SQL(sql)
      select().map(
          row => new Interest(EventTypes.withName(row[String]("event_type")), row[String]("category"))
      ).toSet.toList
    }
  }

}