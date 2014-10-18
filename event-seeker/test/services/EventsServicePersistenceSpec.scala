package services

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.matcher._
import model.EventOrderTypes
import model.EventTypes

/**
 * Tests for persistence functions of the EventsService
 * @author George Georgovassilis
 * 
 */

@RunWith(classOf[JUnitRunner])
class EventsServiceSpec extends Specification with DateParser{

  def fixture =
    new {
      EventsService.createTables()
      val music1 = EventsService.insert(EventsService.fromJson(
        """{"id":0,"eventType":"music","title":"test event 1","city":"Test city 1","start":"1995-12-18T17:30","end":"1995-12-18T19:00","price":10,"categories":["c1","c2"],"performers":["p1","p2","p3"]}"""))
      val music2 = EventsService.insert(EventsService.fromJson(
        """{"id":0,"eventType":"music","title":"test event 2","city":"Test city 2","start":"1995-12-18T17:30","end":"1995-12-18T19:00","price":20,"categories":["c1","c3"],"performers":["p1","p2","p3"]}"""))
      val conference1 = EventsService.insert(EventsService.fromJson(
        """{"id":0,"eventType":"conference","title":"test event 3","city":"Test city 3","start":"1995-12-10T17:30","end":"1995-12-10T19:00","price":20,"categories":["c1","c3"],"performers":["p1","p2","p3"]}"""))
      val sports1 = EventsService.insert(EventsService.fromJson(
        """{"id":0,"eventType":"sports","title":"test event 4","city":"Test city 3","start":"1995-12-18T17:30","end":"1995-12-18T19:00","price":30,"categories":["c1","c2"],"performers":["p1","p2","p3"]}"""))
    }
  

  "EventsService" should {

    "find three cities" in new WithApplication{
      val f = fixture
      val locations = EventsService.findLocations("T")
      (locations contains "Test city 1") must be equalTo(true)
      (locations contains "Test city 2") must be equalTo(true)
      (locations contains "Test city 3") must be equalTo(true)
      locations.length must be equalTo(3)
    }
    
    "retrieve a previously stored event" in new WithApplication {
      val f = fixture
      val event = f.music1;
      val e2 = EventsService.findById(event.id)
      e2.id must be equalTo (event.id)
      e2.city must be equalTo (event.city)
      e2.categories must be equalTo (event.categories)
      e2.performers must be equalTo (event.performers)
      e2.price must be equalTo (event.price)
      e2.eventType must be equalTo (event.eventType)
    }

    "find all events when no constraints are specified" in new WithApplication {
      val f = fixture
      val results = EventsService.findByProperties(Array(), "", 0, None, EventOrderTypes.None)

      results(0).id must be equalTo (f.music1.id)
      results(1).id must be equalTo (f.music2.id)
      results(2).id must be equalTo (f.conference1.id)
      results(3).id must be equalTo (f.sports1.id)
    }
    "find a conference and a sports event in Test city 3" in new WithApplication {
      val f = fixture
      val results = EventsService.findByProperties(Array(), "Test city 3", 0, None, EventOrderTypes.None)

      results(0).id must be equalTo (f.conference1.id)
      results(1).id must be equalTo (f.sports1.id)
    }

    "find two music events for the music event type" in new WithApplication {
      val f = fixture
      val results = EventsService.findByProperties(Array(EventTypes.Music ), "", 0, None, EventOrderTypes.None)

      results(0).id must be equalTo (f.music1.id)
      results(1).id must be equalTo (f.music2.id)
    }
    "find two concert and a conference for those event types" in new WithApplication {
      val f = fixture
      val results = EventsService.findByProperties(Array(EventTypes.Music , EventTypes.Conference), "", 0, None, EventOrderTypes.None)

      results(0).id must be equalTo (f.music1.id)
      results(1).id must be equalTo (f.music2.id)
      results(2).id must be equalTo (f.conference1.id)
    }
    "find three events cheaper than 20" in new WithApplication {
      val f = fixture
      val results = EventsService.findByProperties(Array(), "", 20, None, EventOrderTypes.None)

      results(0).id must be equalTo (f.music1.id)
      results(1).id must be equalTo (f.music2.id)
      results(2).id must be equalTo (f.conference1.id)
    }
    "find a single music event in Test city 2 that is cheaper than 20 and on the 18th December 1995" in new WithApplication {
      val f = fixture
      val results = EventsService.findByProperties(Array(EventTypes.Music), "Test city 2", 20, parseDate("1995-12-18T00:00"),  EventOrderTypes.None)
      results(0).id must be equalTo (f.music2.id)

    }

  }
}
