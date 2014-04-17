package models.repository

import play.api.Play
import play.api.Application
import slick.driver.ExtendedProfile
import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.JdbcBackend.Database


trait DatabaseRepositoryBuilder {

  
  val SLICK_DRIVER = "slick.db.driver"
  val DEFAULT_SLICK_DRIVER = "scala.slick.driver.H2Driver"

  def getRepository(implicit app : Application) : Repository = {
    val driverClass = app.configuration.getString(SLICK_DRIVER).getOrElse(DEFAULT_SLICK_DRIVER)
    val driver = singleton[JdbcProfile](driverClass)
    new Repository(driver)
  }

  def getDatabase(implicit app : Application) = {
    Database.forDataSource(play.api.db.DB.getDataSource())
  }

  private def singleton[T](name : String)(implicit man: Manifest[T]) : T =
    Class.forName(name + "$").getField("MODULE$").get(man.runtimeClass).asInstanceOf[T]

}