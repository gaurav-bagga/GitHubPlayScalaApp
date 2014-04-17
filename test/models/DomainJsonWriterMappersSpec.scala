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
class DomainJsonWritterMappersSpec extends Specification {

  "JsonMappers" should {

    "map repository to json" in {

      val repo = Repository(1234, "repo")

      val repoJson = Json.toJson[Repository](repo)

      val repoBack = Json.fromJson[Repository](repoJson)

      repoBack match {
        case s: JsSuccess[Repository] => {
          val repo = s.get
          repo must not be null

          repo.id must beEqualTo(1234)
          repo.name must beEqualTo("repo")
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "map branch to json" in {

      val branch = Branch("branch")

      val branchJson = Json.toJson[Branch](branch)

      val branchBack = Json.fromJson[Branch](branchJson)

      branchBack match {
        case s: JsSuccess[Branch] => {
          val branch = s.get
          branch must not be null

          branch.name must beEqualTo("branch")
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

    "map branch commit to json" in {

      val branchCommit = BranchCommit("graph", DateTime.now)

      val branchCommitJson = Json.toJson[BranchCommit](branchCommit)

      val branchCommitBack = Json.fromJson[BranchCommit](branchCommitJson)

      branchCommitBack match {
        case s: JsSuccess[BranchCommit] => {
          val branchCommit = s.get
          branchCommit must not be null

          branchCommit.name must beEqualTo("graph")

          branchCommit.modified must beEqualTo(branchCommit.modified)
        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }
    
    
    "map repo branch count to json" in {

      val repoBranchCount = RepositoryBranchCount(Repository(123,"rtbkit"),12)

      val repoBranchCountJson = Json.toJson[RepositoryBranchCount](repoBranchCount)

      val repoBranchCountBack = Json.fromJson[RepositoryBranchCount](repoBranchCountJson)

      repoBranchCountBack match {
        case s: JsSuccess[RepositoryBranchCount] => {
          val repoBranchCount = s.get
          repoBranchCount must not be null

          repoBranchCount.repo.id must beEqualTo(123)
          repoBranchCount.repo.name must beEqualTo("rtbkit")
          repoBranchCount.count must beEqualTo(12)

        }
        case e: JsError => failure("Matching failed " + JsError.toFlatJson(e).toString())
      }
    }

  }
}

