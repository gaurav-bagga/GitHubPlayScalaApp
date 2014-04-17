package service.remote;

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.specification.Step
import org.specs2.specification.Fragments
import play.api.Play
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.JsonMappers._
import scala.concurrent.duration._
import scala.concurrent.duration.DurationConversions._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import scala.concurrent.Await
import models._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Class tests remote calls made to GitHub returns json and it can be converted into domain case classes
 * <br />
 * <b>Note: </b> ensure test repository and data is present in git hub for valid test scenarios. 
 */
@RunWith(classOf[JUnitRunner])
class RemoteServiceSpec extends Specification {

  "Remote Service" should {

    "fetch repistories for a valid user" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.listRepository(user = "rtbkit")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => {
            val list = json.as[List[Repository]]
            list must not be null
            list.size > 0
          }
          case None => failure("Remote service failed to get data")
        }

      }
    }
    
    
     "fetch no repistories for an invalid user" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.listRepository(user = "xyz1234___")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => failure("Remote service for unknown user repository fetched json")
          case None => success
        }

      }
    }
    
    
    "fetch branches for a valid repository" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.getBranches(owner = "rtbkit", repo = "rtbkit")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => {
            val list = json.as[List[Branch]]
            list must not be null
            list.size > 0
          }
          case None => failure("Remote service failed to get data")
        }

      }
    }
    
    
     "fetch no branches for an invalid user" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.getBranches(owner = "xyz1234___", repo = "rtbkit")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => failure("Remote service for unknown user repository fetched json")
          case None => success
        }

      }
    }
     
     
     "fetch no branches for a valid user but invalid repo" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.getBranches(owner = "rtbkit", repo = "xyz1234___")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => failure("Remote service for unknown user repository fetched json")
          case None => success
        }

      }
    } 
    
    
    
    "fetch branch commits for a valid branch" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.getBrancheCommits(owner = "rtbkit", repo = "quickboard", branch = "gh-pages")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => {
            val branchCommit = json.as[BranchCommit]
            branchCommit must not be null
            branchCommit.name must beEqualTo("gh-pages")
            branchCommit.modified must not be null
          }
          case None => failure("Remote service failed to get data")
        }

      }
    }
    
    
    "fetch no branch commits for an invalid branch" in {
      running(FakeApplication()) {
        val remoteJson = RemoteService.getBrancheCommits(owner = "rtbkit", repo = "quickboard", branch = "xyz1234___")
        val remoteJsonValue = Await.result(remoteJson, FiniteDuration(10, SECONDS))

        remoteJsonValue match {
          case Some(json) => failure("Remote service for unknown user repository fetched json")
          case None => success
        }

      }
    } 
    

  }
}
