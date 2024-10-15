package com.nicelydone.passwordmanager.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nicelydone.passwordmanager.model.entity.Password

@Dao
interface PasswordDao {
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun insert(password: Password)

   @Update
   fun update(password: Password)

   @Delete
   fun delete(password: Password)

   @Query("SELECT * FROM password")
   fun getAll(): LiveData<List<Password>>
}