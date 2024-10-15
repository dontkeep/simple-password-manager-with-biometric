package com.nicelydone.passwordmanager.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Password(
   @PrimaryKey(autoGenerate = true)
   @ColumnInfo( name = "id")
   val id: Int = 0,

   @ColumnInfo(name = "title")
   val title: String,

   @ColumnInfo(name = "username")
   val username: String,

   @ColumnInfo(name = "password")
   val password: String,

): Parcelable
