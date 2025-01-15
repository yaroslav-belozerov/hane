package com.hane.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hane.data.remote.ArticleDto
import com.hane.domain.Article

@Dao
interface ArticleDao {
  @Upsert
  suspend fun upsert(article: ArticleEntity): Long

  @Delete
  suspend fun delete(article: ArticleEntity)

  @Query("SELECT * FROM `ArticleEntity` WHERE id = :id")
  suspend fun getArticle(id: Long): ArticleEntity?

  @Query("SELECT * FROM `ArticleEntity`")
  suspend fun getAllCachedArticles(): List<ArticleEntity>
}
