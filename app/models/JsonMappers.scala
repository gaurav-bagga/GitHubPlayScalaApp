package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Maintains JSON reader and writers for domain models
 */
object JsonMappers {

  //Date pattern followed by git hub
  val datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"

  /**
   *   Converts a JSON String to Repository case class
   */
  implicit val repositoryReads: Reads[Repository] = (
	    (JsPath \ "id").read[Int] and
	    (JsPath \ "name").read[String]
    )(Repository.apply _)

  /**
   * Converts Repository case class to JSON
   */
  implicit val repositoryWrites: Writes[Repository] = (
	    (JsPath \ "id").write[Int] and
	    (JsPath \ "name").write[String]
    )(unlift(Repository.unapply))

  /**
   *   Converts a JSON String to Branch case class
   */
  implicit val branchReads: Reads[Branch] =
    (JsPath \ "name").read[String].map(name => new Branch(name))

  /**
   * Converts Branch case class to JSON
   */
  implicit val branchWrites: Writes[Branch] = new Writes[Branch] {
    def writes(branch: Branch) = Json.obj(
      "name" -> branch.name)
  }

  /**
   *   Converts a JSON String to BranchCommit case class
   */
  implicit val branchCommitReads: Reads[BranchCommit] = (
	    (JsPath \ "name").read[String] and
	    (JsPath \ "commit" \ "commit" \ "author" \ "date").read[String].map[DateTime](date => DateTime.parse(date, DateTimeFormat.forPattern(datePattern)))
    )(BranchCommit.apply _)

  /**
   * Converts BracnhCommit case class to JSON
   */
  implicit val branchCommitWrites: Writes[BranchCommit] = new Writes[BranchCommit] {
    def writes(branchCommit: BranchCommit) = Json.obj(
      "name" -> branchCommit.name,
      "commit" -> Json.obj(
        "commit" -> Json.obj(
          "author" -> Json.obj(
            "date" -> DateTimeFormat.forPattern(datePattern).print(branchCommit.modified)
            )
           )
          )
         )
  }
  
  /**
   *   Converts a JSON String to RepositoryBranchCount case class
   */
  implicit val repoBranchCountReads: Reads[RepositoryBranchCount] = (
	    (JsPath \ "id").read[Int] and
	    (JsPath \ "name").read[String] and
	    (JsPath \ "count").read[Int]
    )((id,name,count) => RepositoryBranchCount(Repository(id,name),count))
  

  

  /**
   * Converts RepositoryBranchCount case class to JSON
   */
  implicit val repoBranchCountWrites: Writes[RepositoryBranchCount] = new Writes[RepositoryBranchCount] {
    def writes(repositoryBranchCount: RepositoryBranchCount) = Json.obj(
      "id" -> repositoryBranchCount.repo.id,
      "name"-> repositoryBranchCount.repo.name,
      "count" -> repositoryBranchCount.count
      )
  }
  
}