package services

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.matcher._
import model.EventOrderTypes
import model.EventTypes
import model.Interest

/**
 * Tests for the CategoryService
 * @author George Georgovassilis
 * 
 */
@RunWith(classOf[JUnitRunner])
class CategoryServiceSpec extends Specification {

  def fixture =
    new {
      val event1 = 1
      val event2 = 2
      val event3 = 3

      val event1_categories = List("cat1", "cat2", "cat3")
      val event2_categories = List("cat1", "cat2")
      val event3_categories = List("cat9")

      CategoryService.createTables()
      CategoryService.save(event1, EventTypes.Music, event1_categories)
      CategoryService.save(event2, EventTypes.Music, event2_categories)
      CategoryService.save(event3, EventTypes.Conference, event3_categories)
    }

  "CategoryService" should {

    "find all events for a category" in new WithApplication {
      val f = fixture
      var ids = CategoryService.findEventsForInterests(List(new Interest(EventTypes.Music, "cat1")))
      ids.contains(f.event1) must beTrue
      ids.contains(f.event2) must beTrue
      ids.contains(f.event3) must beFalse

    }

    "find all events for multiple categories" in new WithApplication {
      val f = fixture
      var ids = CategoryService.findEventsForInterests(List(new Interest(EventTypes.Conference, "cat9"), new Interest(EventTypes.Music, "cat3")))
      ids.contains(f.event1) must beTrue
      ids.contains(f.event2) must beFalse
      ids.contains(f.event3) must beTrue

    }

    "not find anything for a foreign category" in new WithApplication {
      val f = fixture
      var ids = CategoryService.findEventsForInterests(List(new Interest(EventTypes.Sports, "catX")))
      ids must beEmpty

    }

    "find all interests for every registered event" in new WithApplication {
      val f = fixture
      val interests = CategoryService.getAllInterest
      interests.contains(new Interest(EventTypes.Music, "cat1")) must beTrue
      interests.contains(new Interest(EventTypes.Music, "cat2")) must beTrue
      interests.contains(new Interest(EventTypes.Music, "cat3")) must beTrue
      interests.contains(new Interest(EventTypes.Conference, "cat9")) must beTrue
    }
  }
}
