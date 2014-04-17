package models.repository

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.specification.Step
import org.specs2.specification.Fragments
import play.api.Play
import models.Search

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class RepositorySpec extends Specification {
  "Search" should {

    "be saved returing the auto increment id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        TestAppDB.init
        TestAppDB.database.withSession {
          implicit session =>
            val search = new Search(None, "rtbkit")
            val searchId = TestAppDB.repository.insert(search)
            val searchFromDb = TestAppDB.repository.findById(searchId)

            searchFromDb.get.id must beEqualTo(searchId)
            searchFromDb.get.search must beEqualTo("rtbkit")

        }
      }
    }

    "find search by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        TestAppDB.init
        TestAppDB.database.withSession {
          implicit session =>
            val search = new Search(None, "rtbkit")
            val searchId = TestAppDB.repository.insert(search)
            val searchFromDb = TestAppDB.repository.findById(searchId)

            searchFromDb.get.id must beEqualTo(searchId)
            searchFromDb.get.search must beEqualTo("rtbkit")

        }
      }
    }

    "find search by search term" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        TestAppDB.init
        TestAppDB.database.withSession {
          implicit session =>
            val search = new Search(None, "rtbkit")
            val searchId = TestAppDB.repository.insert(search)
            val searchFromDb = TestAppDB.repository.findBySearchTerm("rtbkit")

            searchFromDb.get.id must beEqualTo(searchId)
            searchFromDb.get.search must beEqualTo("rtbkit")

        }
      }
    }
    
    "find all search" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        TestAppDB.init
        TestAppDB.database.withSession {
          implicit session =>
            val search1Id = TestAppDB.repository.insert(new Search(None, "rtbkit"))
            val search2Id = TestAppDB.repository.insert(new Search(None, "graph"))
            
            val searches = TestAppDB.repository.findAll
            
            searches.size must beEqualTo(2)

            searches(0).id must beEqualTo(search1Id)
            searches(0).search must beEqualTo("rtbkit")
            
            searches(1).id must beEqualTo(search2Id)
            searches(1).search must beEqualTo("graph")

        }
      }
    }

    "delete search by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        TestAppDB.init
        TestAppDB.database.withSession {
          implicit session =>
            val search = new Search(None, "rtbkit")
            val searchId = TestAppDB.repository.insert(search)
            val searchFromDb = TestAppDB.repository.findById(searchId)

            searchFromDb.get.id must beEqualTo(searchId)
            searchFromDb.get.search must beEqualTo("rtbkit")

            TestAppDB.repository.delete(searchFromDb.get)

            val searchFromDbAfterDelete = TestAppDB.repository.findById(searchId)

            searchFromDbAfterDelete must beEqualTo(None)

        }
      }
    }

    "delete search by search term" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        TestAppDB.init
        TestAppDB.database.withSession {
          implicit session =>
            val search = new Search(None, "rtbkit")
            val searchId = TestAppDB.repository.insert(search)
            val searchFromDb = TestAppDB.repository.findById(searchId)

            searchFromDb.get.id must beEqualTo(searchId)
            searchFromDb.get.search must beEqualTo("rtbkit")

            TestAppDB.repository.deleteBySerachTerm(searchFromDb.get.search)

            val searchFromDbAfterDelete = TestAppDB.repository.findById(searchId)

            searchFromDbAfterDelete must beEqualTo(None)

        }
      }
    }

  }
}
