package com.hane.data.remote

import android.util.Log
import androidx.core.text.HtmlCompat
import com.hane.domain.Comment
import com.hane.renderHtml
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
  val by: String? = null,
  val id: Long? = null,
  val kids: List<Long>? = null,
  val parent: Long? = null,
  val text: String? = null,
  val time: Long? = null,
  val type: String? = null,
)

fun CommentDto.toDomainModel(): Comment {
  return Comment(
    by = by.orEmpty(),
    id = id!!,
    kids = kids.orEmpty(),
    text = HtmlCompat.fromHtml(text.orEmpty().trim(), HtmlCompat.FROM_HTML_MODE_COMPACT).renderHtml(),
  )
}
