import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import controllers.API

/**
 * Minor integration test; will start the application at a port and connect to it
 * @author George Georgovassilis
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boom")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Search for events by")
    }
    
    "should import feeds and serve them" in new WithBrowser{
      var post = route(FakeRequest("POST", "/api/events/concerts?url=http://localhost:"+port+"/assets/feeds/Concerts.json")).get
      contentAsString(post) must beEqualTo("ok")
      val query = route(FakeRequest("GET", "/api/events?eventTypes=music&location=City 1&maxPrice=0&date=&orderBy=price")).get
      contentAsString(query) must contain ("City 1")
    }
  }
}
