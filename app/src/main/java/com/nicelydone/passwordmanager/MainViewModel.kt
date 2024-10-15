package com.nicelydone.passwordmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nicelydone.passwordmanager.model.entity.Password
import com.nicelydone.passwordmanager.model.repository.PasswordRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
   private val mPasswordRepository: PasswordRepository = PasswordRepository(application)
   val passwordList: LiveData<List<Password>> = mPasswordRepository.getAllPassword()

   fun insert(password: Password) = viewModelScope.launch {
      mPasswordRepository.insert(password)
   }

   fun update(password: Password) = viewModelScope.launch {
      mPasswordRepository.update(password)
   }

   fun delete(password: Password) = viewModelScope.launch {
      mPasswordRepository.delete(password)
   }
}