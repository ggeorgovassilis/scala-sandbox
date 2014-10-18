package services

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import model.Event
import model.EventTypes

/**
 * Tests for JSON functions of the EventsService
 * @author George Georgovassilis
 * 
 */

@RunWith(classOf[JUnitRunner])
class EventsServiceJsonSpec extends Specification with DateParser{

  "Event" should {
    "serialize to json" in {
        val startDate = parseDate("1995-12-18T17:30")
        val endDate = parseDate("1995-12-18T19:00")
    	val e = new Event(123, EventTypes.Music, "test event", Array("p1","p2","p3"), "Test city", Array("c1","c2"), startDate.get, endDate.get,10);
    	EventsService.toJson(e) must be equalTo(
    	"""{"id":123,"eventType":"music","title":"test event","city":"Test city","start":"1995-12-18T17:30","end":"1995-12-18T19:00","price":10,"categories":["c1","c2"],"performers":["p1","p2","p3"]}"""    
    	)
    }
  }

  "Event" should {
    "deserialize from json" in {
        val startDate = parseDate("1995-12-18T17:30")
        val endDate = parseDate("1995-12-18T19:00")
    	val e = EventsService.fromJson(
    	"""{"id":123,"eventType":"music","title":"test event","city":"Test city","start":"1995-12-18T17:30","end":"1995-12-18T19:00","price":10,"categories":["c1","c2"],"performers":["p1","p2","p3"]}""" )
    	e.id must be equalTo(123)
    	e.city must be equalTo("Test city")
    	e.title must be equalTo("test event")
    	e.categories(0) must be equalTo("c1")
    	e.categories(1) must be equalTo("c2")

    	e.start must be equalTo(startDate.get)
    	e.end  must be equalTo(endDate.get)
    	e.eventType  must be equalTo(EventTypes.Music)
    	e.price  must be equalTo(10)
    	e.performers(0)  must be equalTo("p1")
    	e.performers(1)  must be equalTo("p2")
    	e.performers(2)  must be equalTo("p3")
    }
  }
}
