package service

import models.repository.ApplicationRepository
import play.api.Logger
import models.Search
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import service.remote.RemoteService
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.JsonMappers._
import models.RepositoryBranchCount
import models.Repository
import scala.util.Success
import scala.util.Failure
import models.Branch
import models.RepositoryBranchCount
import models.RepositoryBranchCount
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.collection.mutable.MutableList
import models.BranchCommit
import models.Branch

object Service {

  val applicationrepository = new ApplicationRepository
  
  def searchRepo(searchTerm : String) : Future[List[RepositoryBranchCount]] = {
    val dbFutureResult = Future {
		    applicationrepository.database.withSession {
		          implicit session =>
		            val searchInDb = applicationrepository.repository.findBySearchTerm(searchTerm);
		            
		            searchInDb match {
		              case Some(search) => Logger.info(s"Search term $searchTerm found in database")
		              case None => {
		                Logger.info(s"Saving new search term $searchTerm")
		                applicationrepository.repository.insert(Search(None,searchTerm))
		              }  
		            }
		
		        }
    }
    
    return (for {
      f <- RemoteService.listRepository(searchTerm)
      s <- matchBranchCountForEachRepo(searchTerm, f)
    } yield s)
    
  }
  
  def probeBranch(owner : String, repo : String) : Future[List[BranchCommit]] = {
    return (for {
      f <- RemoteService.getBranches(owner, repo)
      s <- matchBranchCommitEachBranch(owner, repo, f)
    } yield s)
    
  }
  
  def home : Future[List[Search]] = {
    val dbFutureResult = Future {
	    applicationrepository.database.withSession {
	          implicit session =>
	            applicationrepository.repository.findAll
	    }
    }
    
    return dbFutureResult;
  }
  
  private def matchBranchCountForEachRepo(searchTerm : String,json : Option[JsValue]) : Future[List[RepositoryBranchCount]] = {
    json match {
          case Some(ojson) => {
            val list = ojson.as[List[Repository]]
            val rbc = MutableList[RepositoryBranchCount]()
            list.foreach(repo =>{
              rbc += RepositoryBranchCount(repo,Await.result(RemoteService.getBranches(searchTerm,repo.name).map(json => json.get.as[List[Branch]].size), FiniteDuration(5,SECONDS)))
            })
            Future{rbc.toList}
          }
          
          case None => Future{List[RepositoryBranchCount]()}
        }
  }
  
  private def matchBranchCommitEachBranch(owner : String, repo : String,json : Option[JsValue]) : Future[List[BranchCommit]] = {
    json match {
          case Some(ojson) => {
            val list = ojson.as[List[Branch]]
            val bc = MutableList[BranchCommit]()
            list.foreach(branch =>{
              bc += BranchCommit(branch.name,Await.result(RemoteService.getBrancheCommits(owner, repo, branch.name).map(json => json.get.as[BranchCommit].modified), FiniteDuration(5,SECONDS)))
            })
            Future{bc.toList}
          }
          
          case None => Future{List[BranchCommit]()}
        }
  }
  
}