package models.repository

import scala.slick.driver.JdbcProfile
import scala.slick.lifted.MappedTo
import models.Search
import models.Search

trait Profile {
  val profile: JdbcProfile
}

trait SearchComponent {

  this: Profile =>

  import profile.simple._

  class Searches(tag: Tag) extends Table[Search](tag, "SEARCHES") {
    def id = column[Option[Int]]("Search_Id", O.PrimaryKey, O.AutoInc)
    def search = column[String]("Search")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, search) <> (Search.tupled, Search.unapply)

  }
  val searches = TableQuery[Searches]
  val searchIdIns = searches returning(searches.map(_.id))
  
  def insert(search: Search)(implicit session: Session) = {
    searchIdIns += search 
  }
  
  def findAll()(implicit session: Session) = {
    val search = for {
      s <- searches
    } yield s

    search.list
  }
  
  def findBySearchTerm(searchTerm: String)(implicit session: Session) = {
    val search = for {
      s <- searches if s.search === searchTerm
    } yield s

    search.firstOption
  }
  
  def findById(id: Option[Int])(implicit session: Session) = {
    val search = for {
      s <- searches if s.id === id
    } yield s

    search.firstOption
  }
  
  def delete(search: Search)(implicit session: Session) = {
    val searchToDelete = for {
      s <- searches if s.id === search.id
    } yield s

    searchToDelete.delete
  }
  
  def deleteBySerachTerm(searchTerm: String)(implicit session: Session) = {
    val searchToDelete = for {
      s <- searches if s.search === searchTerm
    } yield s

    searchToDelete.delete
  }
}

class Repository(override val profile: JdbcProfile) extends SearchComponent with Profile {
  import profile.simple._
  import scala.slick.jdbc.meta._
  
  def create(implicit session: Session): Unit = {
    if (MTable.getTables.list().size < 1)
    	(searches.ddl).create //helper method to create all tables
  }
}