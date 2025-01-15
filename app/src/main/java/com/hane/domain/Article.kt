package com.hane.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Article (
  @PrimaryKey val id: Long,
  val by: String,
  val score: String,
  val time: String,
  val title: String,
  val type: String,
  val url: String?,
  val kids: List<Long> = emptyList(),
  val cached: Boolean
)
