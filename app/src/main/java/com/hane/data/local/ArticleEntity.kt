package com.hane.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hane.domain.Article
import com.hane.format
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ArticleEntity (
  @PrimaryKey val id: Long,
  val by: String,
  val score: Long? = null,
  val time: Long? = null,
  val title: String? = null,
  val type: String? = null,
)

fun ArticleEntity.toDomainModel(): Article {
  return Article(
    by = by,
    id = id,
    score = score?.toString() ?: "",
    time = time.format(),
    title = title ?: "",
    url = null,
    type = type ?: "",
    cached = true
  )
}
