package com.nicelydone.passwordmanager.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.nicelydone.passwordmanager.model.dao.PasswordDao
import com.nicelydone.passwordmanager.model.database.PasswordDatabase
import com.nicelydone.passwordmanager.model.entity.Password
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepository(application: Application) {
   private val passwordDao: PasswordDao = PasswordDatabase.getInstanceDatabase(application).passwordDao()

   fun getAllPassword(): LiveData<List<Password>> = passwordDao.getAll()

   suspend fun insert(password: Password) {
      withContext(Dispatchers.IO) {
         passwordDao.insert(password)
      }
   }

   suspend fun update(password: Password) {
      withContext(Dispatchers.IO) {
         passwordDao.update(password)
      }
   }

   suspend fun delete(password: Password) {
      withContext(Dispatchers.IO) {
         passwordDao.delete(password)
      }
   }
}