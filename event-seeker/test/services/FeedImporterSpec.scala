package services

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import model.Event
import play.api.libs.json.Reads

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Tests for the FeedImporter
 * @author George Georgovassilis
 * 
 */

@RunWith(classOf[JUnitRunner])
class FeedImporterSpec extends Specification with DateParser {

  "FeedImporter" should {

    "import concerts" in new WithBrowser {
      val events = FeedImporter.getConcertsFrom("http://localhost:"+port+"/assets/feeds/Concerts.json")

      val event = events(1)
      event.title must beEqualTo("Concert 2")
      event.performers(0) must beEqualTo("Artist 2")
      event.city must beEqualTo("City 1")
      event.categories(0) must beEqualTo("genre 3")
      event.categories(1) must beEqualTo("genre 1")
      event.categories(2) must beEqualTo("genre 4")
      
      toGmt(event.start) must beEqualTo("2013-01-09T21:00")
      toGmt(event.end) must beEqualTo("2013-01-09T23:00")
      event.price  must beEqualTo(2)
    }
  }

}
