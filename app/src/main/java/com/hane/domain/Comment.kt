package com.hane.domain

import androidx.compose.ui.text.AnnotatedString

data class Comment(
  val id: Long,
  val by: String,
  val kids: List<Long>,
  val text: AnnotatedString,
)
