package controllers

import play.api._
import play.api.mvc._
import service.Service
import scala.concurrent.Future
import models.Repository
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.JsonMappers._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc.Action
import models.RepositoryBranchCount
import models.BranchCommit
import play.api.data._
import play.api.data.Forms._
import models.Search

object Application extends Controller {


  def index =  {
    val future : Future[List[Search]]  = Service.home
     Action.async {
      future.map(list => Ok(views.html.index(list.slice(0,3),list.slice(3,list.size)))) 
    }
    
  }
  
  def repo(username : String) = { 
    val future : Future[List[RepositoryBranchCount]] = Service.searchRepo(username)
    Action.async { implicit request =>
      future.map(list => {
	        render {
		    case Accepts.Html() => Ok(views.html.repo(username,list))
		    case Accepts.Json() => Ok(Json.toJson(list))
		  }
        }
      )
    }
  }
  
  def branch(owner : String,repo : String) = {
    val future : Future[List[BranchCommit]] = Service.probeBranch(owner, repo)
    Action.async { implicit request =>
      future.map(list => {
        render {
		    case Accepts.Html() => Ok(views.html.branch(list))
		    case Accepts.Json() => Ok(Json.toJson(list))
		  }
      })
    }
  }

}