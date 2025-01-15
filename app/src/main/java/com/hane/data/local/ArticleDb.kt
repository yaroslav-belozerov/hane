package com.hane.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hane.domain.Article

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticleDb: RoomDatabase() {
  abstract fun dao(): ArticleDao
}
