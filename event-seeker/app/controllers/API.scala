package controllers

import play.api.libs.json.Json
import play.api._
import play.api.mvc._
import anorm._
import play.api.db.DB
import play.api.Play.current
import services.EventsService
import java.util.Date
import java.text.SimpleDateFormat
import services.DateParser
import services.FeedImporter
import model.EventTypes
import play.api.libs.json.JsArray
import model.EventOrderTypes
import services.CategoryService
import model.Event
import play.api.libs.json.JsObject
import play.api.libs.json.JsArray
import play.api.libs.json.JsArray
import model.Interest

/**
 * Exposes a JSON/REST API for importing and querying events
 * @author George Georgovassilis
 */

object API extends Controller with DateParser {

  /**
   * Utility that converts a list of events to their JSON representation
   * @param events 
   */
  private def eventsToJsonString(events:List[Event]):String={
	  val arr = Array[String]()
      val json = events.map(event=>EventsService.toJson(event))
      json.mkString("[",",","]")
  }

  /**
   * Returns a list of locations that match the 'location' argument
   * @param location part of the location name
   */
  def findLocations(location: String) = Action {
    DB.withTransaction { implicit c =>
      val locations = EventsService.findLocations(location)
      Ok(Json.stringify(Json.toJson(locations.toList)))
    }
  }
  
  /**
   * Returns a list of events that match one or more constraints
   * @param eventTypesString comma separated list of event types. Values are from EventTypes
   * @param location Event locations. Conventions from findLocation(String) apply
   * @param maxPrice Upper price boundary for events. Leave at 0 for no constraint
   * @param date Date of the event. ISO date format YYYY-MM-DDThh:mm 
   * @param orderBy Order results by this column. Values from EventOrderTypes
   */
  def findEvents(eventTypesString: String, location: String, maxPrice: Long, date: String, orderBy: String) = Action {
    DB.withTransaction { implicit c =>

      var eventDate = parseDate(date)
      // splitting an empty string still return an array with a single empty string -> filter that out
      val eventTypes = eventTypesString.split(",").filter((s: String) => (!s.isEmpty)).map(et=>EventTypes.withName(et))

      val events = EventsService.findByProperties(eventTypes, location, maxPrice, eventDate, EventOrderTypes.withName(orderBy));
      val json = eventsToJsonString(events)
      Ok(json).withHeaders("Content-Type" -> "application/json")
    }
  }

  /**
   * Utility for storing events and learning their categories
   * @param events List of events to store. Previous events of the same event type will be erased
   * @param eventType 
   */
  private def ingestEvents(events: List[Event], eventType: EventTypes.EventType) {
    DB.withTransaction { implicit c =>
      CategoryService.createTablesIfDontExist
      EventsService.replaceEvents(eventType, events)
      CategoryService.deleteCategoriesForEventType(eventType)
      CategoryService.updateCategoriesForEvents(events)
    }
  }

  /**
   * Imports concerts from a remote JSON feed
   * @param url JSON feed url
   */
  def importConcerts(url: String) = Action {
    ingestEvents(FeedImporter.getConcertsFrom(url), EventTypes.Music);
    Ok("ok")
  }

  /**
   * Imports conferences from a remote JSON feed
   * @param url JSON feed url
   */
  def importConferences(url: String) = Action {
    ingestEvents(FeedImporter.getConferencesFrom(url), EventTypes.Conference);
    Ok("ok")
  }

  /**
   * Imports sports events from a remote JSON feed
   * @param url JSON feed url
   */
  def importSports(url: String) = Action {
    ingestEvents(FeedImporter.getSportsFrom(url), EventTypes.Sports)
    Ok("ok")
  }

  /**
   * Returns a list of all event type / category combinations of existing events
   */
  def findInterests() = Action {
    DB.withTransaction { implicit c =>
      val interestsArray: List[JsObject] = CategoryService.getAllInterest.map(interest => {
        val jsInterest = Json.obj("eventType" -> interest.eventType.toString(), "category" -> interest.category)
        jsInterest
      })
      val json = JsArray(interestsArray)
      Ok(Json.stringify(json)).withHeaders("Content-Type" -> "application/json")
    }
  }

  /**
   * Returns a list of events that match the given interests
   * @param listOfInterests is string of the form "eventType1:category1,eventType2:category2,..."
   */
  def findEventsByInterest(listOfInterests: String)=Action{
    DB.withTransaction { implicit c =>
      var eventTypeCategoryPairs=listOfInterests.split(",").toList
      if (listOfInterests.isEmpty()){
        eventTypeCategoryPairs = List()
      }
      println(eventTypeCategoryPairs)
      val interests = eventTypeCategoryPairs.map(s=>{
        val pair = s.split(":")
        new Interest(EventTypes.withName(pair(0)), pair(1))
      })
      val eventIds = CategoryService.findEventsForInterests(interests.toList)
      val events = EventsService.findByIds(eventIds);
      Ok(eventsToJsonString(events)).withHeaders("Content-Type" -> "application/json")
    }
  }

}