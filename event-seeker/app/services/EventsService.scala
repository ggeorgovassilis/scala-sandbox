package services

import anorm._
import play.api.db.DB
import play.api.Play.current

import model._
import anorm.SqlParser.{ str, int }
import java.util.Date
import play.api.libs.json._

/**
 * Persistence functionality for events
 * @author George Georgovassilis
 */
object EventsService extends DateParser {

  /**
   * Constructs an event from its JSON representation
   * @param s
   */
  def fromJson(s: String): Event = {
    val json = Json.parse(s)
    val id = (json \ "id").as[Long]
    val city = (json \ "city").as[String]
    val title = (json \ "title").as[String]
    val start = parseDate((json \ "start").as[String])
    val end = parseDate((json \ "end").as[String])
    val eventType = (json \ "eventType").as[String]
    val price = (json \ "price").as[Long]
    val categories = (json \ "categories").as[Array[String]]
    val performers = (json \ "performers").as[Array[String]]
    return Event(id, EventTypes.withName(eventType), title, performers, city, categories, start.get, end.get, price)
  }

  /**
   * Convert an event to JSON
   * @param event
   */
  def toJson(event: Event): String = {
    def toJsonArray(list: List[String]): JsArray = {
    
      val arr = list.map(s => JsString(s))
      return JsArray(arr)
    }

    val result = Json.obj(
      "id" -> event.id,
      "eventType" -> event.eventType.toString(),
      "title" -> event.title,
      "city" -> event.city,
      "start" -> toGmt(event.start),
      "end" -> toGmt(event.end),
      "price" -> event.price,
      "categories" -> toJsonArray(event.categories.toList),
      "performers" -> toJsonArray(event.performers.toList))
    return Json.stringify(result)

  }

  /**
   * Persist a new event. Will assign a new ID to the event
   */
  def insert(event: Event): Event = {
    val id: Option[Long] = DB.withConnection { implicit c =>
      val id = SQL("insert into Event(eventType, price, city, startdate, json) values ({eventType}, {price}, {city}, {startdate}, {json})")
        .on('eventType -> event.eventType.toString(),
          'price -> event.price,
          'city -> event.city,
          'startdate -> event.start,
          'json -> toJson(event)).executeInsert()
    event.id = id.get
    val update = SQL("update Event set json={json} where id={id}")
    .on('id->event.id)
    .on('json->toJson(event))
    update.executeUpdate()
    id
    }
    event
  }

  /**
   * Create database tables and deletes any old ones
   */
  def createTables() {

    // no big deal if drop fails
    DB.withConnection { implicit c =>
      try {
        SQL("drop table Event").executeUpdate()
      } catch {
        case e: Exception => {}
      }
      createTablesIfDontExist()
    }
  }

  /**
   * Create database tables only if they don't exist
   */
  def createTablesIfDontExist() {
    DB.withConnection { implicit c =>
      try {
        SQL("create table Event(id bigint auto_increment, eventType varchar(20), price int, city varchar(255), startdate timestamp, json varchar(4096))").executeUpdate
      } catch {
        case e: Exception => {}
      }
    }
  }

  /**
   * Find an event by its ID
   * @param id
   */
  def findById(id: Long): Event = {
    DB.withConnection { implicit c =>
      val select = SQL(
        """
    		  select * from Event e 
    		  where e.id= {id}
        """).on("id" -> id)
      val events = select.executeQuery.apply.toList.map(row => {
        val json = row[String]("json")
        val e = fromJson(json)
        e.id = row[Long]("id")
        e
      })
      events(0)
    }
  }

  /**
   * Find a bunch of events by their IDs
   * @param ids
   */
  def findByIds(ids: List[Long]): List[Event] = {
    ids.map(id => findById(id))
  }

  /**
   * Find events by specified constraints
   * @param eventTypes Events must be of one of these event types
   * @param location exact location match
   * @param maxPrice upper bound on price. Set 0 for no constraint
   * @param date Optionally specify a date on which the event occurs
   * @param orderBy Event will be sorted by this property
   */
  def findByProperties(eventTypes: Array[EventTypes.EventType], location: String, maxPrice: Long, date: Option[Date], orderBy: EventOrderTypes.Order): List[Event] = {
    DB.withConnection { implicit c =>
      var query = "select * from Event e where 1 = 1 "
      if (eventTypes.length > 0) {
        query = query + "AND eventType in ("
        var prefix = ""
        for (et <- eventTypes) {
          query = query + prefix + "'" + et + "'"
          prefix = ","
        }
        query = query + ") "
      }
      if (location.length() > 0) {
        query = query + "AND lower(city) = '" + location.toLowerCase() + "' "
      }
      if (maxPrice > 0) {
        query = query + "AND price <= " + maxPrice + " "
      }
      if (date.isDefined) {
        val d = date.get
        query = query + "AND YEAR(startdate) = " + (d.getYear() + 1900);
        query = query + "AND MONTH(startdate) = " + (d.getMonth() + 1);
        query = query + "AND DAY_OF_MONTH(startdate) = " + d.getDate() + " ";
      }
      val orderColumn = orderBy match {
        case EventOrderTypes.Price => "price asc"
        case EventOrderTypes.Date => "startdate asc"
        case _ => "id"
      };
      query = query + "order by " + orderColumn
      val select = SQL(query)
      select().map(row => {
        val json = row[String]("json")
        val event = fromJson(json)
        event.id = row[Long]("id")
        event
      }).toList
    }
  }

  /**
   * Find locations of events that start with the provided location name
   * @param locationName fragment of the location name
   */
  def findLocations(locationName: String): List[String] = {
    DB.withConnection { implicit c =>
      val select =
        SQL("select distinct city from Event where lower(city) like {location} order by city")
          .on("location" -> ("%" + locationName.toLowerCase() + "%"));

      select().map(row => row[String]("city")).toList
    }
  }

  /**
   * Removes old events of the specified type and inserts new ones
   * @param eventType the type of events to delete
   * @param events the new events to persist
   */
  def replaceEvents(eventType: EventTypes.EventType, events: List[Event]) {
    createTablesIfDontExist
    DB.withConnection { implicit c =>
      SQL("delete from Event where eventtype = {t}").on("t" -> eventType.toString()).executeUpdate;
      events.foreach(e => insert(e))
    }

  }
}