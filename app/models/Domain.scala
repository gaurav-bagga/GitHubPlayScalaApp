package models

import org.joda.time.DateTime

/**
 * Domain models for the system
 */

case class Repository(id:Int,name:String)
case class Branch(name : String)
case class BranchCommit(name : String,modified : DateTime)
case class Search(id: Option[Int],search: String)
case class RepositoryBranchCount(repo : Repository,count : Int)
