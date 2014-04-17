package models.repository

import play.api.Play.current

object TestAppDB extends DatabaseRepositoryBuilder{

  private var dbWrapper:DbWrapper = _
  
  
  def init() = {
    dbWrapper = new DbWrapper()
  }
  
  private class DbWrapper {
    lazy val database = getDatabase
    lazy val repository = getRepository
  }
  
  def database = dbWrapper.database
  
  def repository = dbWrapper.repository
  
}