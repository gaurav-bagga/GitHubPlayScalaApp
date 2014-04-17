package models.repository

import play.api.Play.current

class ApplicationRepository extends DatabaseRepositoryBuilder{

  lazy val database = getDatabase
  lazy val repository = getRepository
}