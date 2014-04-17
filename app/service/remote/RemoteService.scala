package service.remote

import play.api.libs.ws._
import scala.concurrent.Future
import play.api.Play
import play.Configuration
import play.Application
import play.Configuration
import play.api.http.HeaderNames.ACCEPT
import play.api.http.Status
import play.api.libs.json.JsValue
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import play.api.Logger
import com.ning.http.client.Realm.AuthScheme._
/**
 * Manages remote REST calls to GitHub
 */
object RemoteService {

  /**
   * The base url
   */
  val url: Option[String] = Play.current.configuration.getString("remote.service")
  
  /**
   * Simple response processor checks for response status, if OK extracts the json and sends back,
   * else if NOT FOUND simply returns None
   */
  val defaultResponseProcessor  = (response : Response) => {
          response.status match {
            case Status.OK => {
              Logger.info("Json response retrieved")
              Some(response.json)
            }
            case _ => {
              Logger.info("Json response not found reason " + response.status.toInt)
              None
            }
          }
        }

  /**
   * Retrieves repository list by user
   */
  def listRepository(user: String) = {
    val listRepositoryUrl = url.flatMap(url => Option(url + s"/users/$user/repos"))
    processRemote(listRepositoryUrl, defaultResponseProcessor)
  }

  /**
   * Retrieves branch information for a user and a repository
   */
  def getBranches(owner: String, repo: String) = {
    val getBranchesUrl = url.flatMap(url => Option(url + s"/repos/$owner/$repo/branches"))
    processRemote(getBranchesUrl, defaultResponseProcessor)
  }

  /**
   * Retrieves commits for a repository for a branch
   */
  def getBrancheCommits(owner: String, repo: String, branch: String) = {
    val getBranchCommitUrl = url.flatMap(url => Option(url + s"/repos/$owner/$repo/branches/$branch"))
    processRemote(getBranchCommitUrl, defaultResponseProcessor)
  }

  /**
   * Helper method to evaluate final URL and make a REST call
   * function taking Response object can be customized to handle complex processes,
   * for trivial cases we use defaultResponseProcessor
   */
  private def processRemote(serviceUrl: Option[String], f : (Response) => Option[JsValue]): Future[Option[JsValue]] = {
    serviceUrl match {
      case Some(serviceUrl) => {
        val responseFuture = WS.url(serviceUrl).get

        responseFuture.recover({
          case e: Exception => Logger.error(s"Problem in fetching json for url $serviceUrl" + e.getMessage())
        })

        responseFuture.map(f)

      }
      case None => {
        Logger.error("Url not found !!!!!")
        Future { None }
      }
    }
  }
}