package com.nicelydone.passwordmanager.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nicelydone.passwordmanager.model.dao.PasswordDao
import com.nicelydone.passwordmanager.model.entity.Password

@Database(entities = [Password::class], version = 1)
abstract class PasswordDatabase: RoomDatabase() {
   abstract fun passwordDao(): PasswordDao

   companion object {
      @Volatile
      private var INSTANCE: PasswordDatabase? = null

      @JvmStatic
      fun getInstanceDatabase(context: Context): PasswordDatabase {
         if (INSTANCE == null) {
            synchronized(PasswordDatabase::class.java) {
               INSTANCE = Room.databaseBuilder(
                  context.applicationContext,
                  PasswordDatabase::class.java,
                  "password_database"
               ).build()
            }
         }
         return INSTANCE as PasswordDatabase
      }
   }
}