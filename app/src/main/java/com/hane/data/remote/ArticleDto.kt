package com.hane.data.remote

import androidx.room.PrimaryKey
import com.hane.data.local.ArticleEntity
import com.hane.domain.Article
import com.hane.format
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Serializable
data class ArticleDto (
  val by: String? = null,
  val id: Long? = null,
  val score: Long? = null,
  val time: Long? = null,
  val title: String? = null,
  val type: String? = null,
  val url: String? = null,
  val kids: List<Long>? = null
)

fun ArticleDto.toDomainModel(): Article {
  return Article(
    by = by.orEmpty(),
    id = id!!,
    score = score?.toString() ?: "",
    time = time.format(),
    title = title.orEmpty(),
    type = type.orEmpty(),
    url = url,
    kids = kids.orEmpty(),
    cached = false
  )
}

fun ArticleDto.toEntity(): ArticleEntity {
  return ArticleEntity(
    by = by.orEmpty(),
    id = id!!,
    score = score,
    time = time,
    title = title,
    type = type,
  )
}