import play.api.GlobalSettings
import play.api.Application
import models.repository.DatabaseRepositoryBuilder

object Global extends GlobalSettings with DatabaseRepositoryBuilder{

  override def onStart(app: Application) {
    implicit val application = app
    lazy val database = getDatabase
    lazy val repository = getRepository
    database.withSession {
      implicit session =>
        repository.create
    }
  }
}