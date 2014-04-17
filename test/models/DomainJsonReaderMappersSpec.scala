package models;

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.specification.Step
import org.specs2.specification.Fragments
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.JsonMappers._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Class tests the JSON mappers used to map JSON from GITHUB to domain models.
 */
@RunWith(classOf[JUnitRunner])
class DomainJsonReaderMappersSpec extends Specification {

  val jsonRepo = Json.parse("""
            {"id" : 123213213, "name" : "repo"}
            """)

  val jsonListRepo = Json.parse("""
            [{"id" : 123213213, "name" : "repo1"},{"id" : 123213214, "name" : "repo2"}]
            """)

  val jsonBranch = Json.parse("""
            {"name" : "branch"}
            """)

  val jsonListBranch = Json.parse("""
            [{"name" : "branch1"},{"name" : "branch2"}]
            """)

  val jsonBranchCommit = Json.parse("""
      {
		"name":"gh-pages",
		"commit":{
			"commit":{
				"author":{
					"date":"2014-02-25T17:34:21Z"
					}
				}
		}
      }
      """)

  val jsonInvalid = Json.parse("""
            {"xyz" : "xyz"}
            """)

  "JsonMappers" should {

    "map repository from json" in {

      val result = jsonRepo.validate[Repository]

      result match {
        case s: JsSuccess[Repository] => {
          val repo = s.get
          repo must not be null
          repo.id must beEqualTo(123213213)
          repo.name must beEqualTo("repo")
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "map list of repositories from json" in {

      val result = jsonListRepo.validate[List[Repository]]

      result match {
        case s: JsSuccess[List[Repository]] => {
          val repo = s.get
          repo must not be null
          repo.size must beEqualTo(2)
          repo(0).id must beEqualTo(123213213)
          repo(0).name must beEqualTo("repo1")
          repo(1).id must beEqualTo(123213214)
          repo(1).name must beEqualTo("repo2")
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "not map repository from invalid json" in {

      val result = jsonInvalid.validate[Repository]

      result match {
        case s: JsSuccess[Repository] => failure(" Should not have matched Repository")
        case e: JsError => success
      }
    }

    "map branch from json" in {

      val result = jsonBranch.validate[Branch]

      result match {
        case s: JsSuccess[Branch] => {
          val branch = s.get
          branch must not be null
          branch.name must beEqualTo("branch")
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "map list of branches from json" in {

      val result = jsonListBranch.validate[List[Branch]]

      result match {
        case s: JsSuccess[List[Branch]] => {
          val branch = s.get
          branch must not be null
          branch.size must beEqualTo(2)
          branch(0).name must beEqualTo("branch1")
          branch(1).name must beEqualTo("branch2")
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "not map branch from invalid json" in {

      val result = jsonInvalid.validate[Branch]

      result match {
        case s: JsSuccess[Branch] => failure(" Should not have matched Branch")
        case e: JsError => success
      }
    }

    "map branch commit from json" in {

      val result = jsonBranchCommit.validate[BranchCommit]

      result match {
        case s: JsSuccess[BranchCommit] => {
          val branchCommit = s.get
          branchCommit must not be null
          branchCommit.name must beEqualTo("gh-pages")

          val date = DateTime.parse("2014-02-25T17:34:21Z", DateTimeFormat.forPattern(JsonMappers.datePattern))

          branchCommit.modified must beEqualTo(date)
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "not map branch commit from invalid json" in {

      val result = jsonInvalid.validate[BranchCommit]

      result match {
        case s: JsSuccess[BranchCommit] => failure(" Should not have matched BranchCommit")
        case e: JsError => success
      }
    }

  }
}
