package com.hane

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.hane.DateFormat.formatter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object C {
  object Net {
    const val BaseUrl = "https://hacker-news.firebaseio.com/v0/"
  }

  object UI {
    val dimPrimary
      @Composable get() = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)

    val dimOnBackground
      @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
  }
}

object DateFormat {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
}

fun Long?.format(): String {
  return this?.let {
    Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter)
  } ?: ""
}

fun CharSequence.renderHtml(): AnnotatedString = buildAnnotatedString {
  if (this@renderHtml !is Spanned) {
    append(this.toString())
    return@buildAnnotatedString
  }

  val spanned = this@renderHtml
  append(spanned.toString())
  getSpans(0, spanned.length, Any::class.java).forEach { span ->
    val start = getSpanStart(span)
    val end = getSpanEnd(span)
    when (span) {
      is StyleSpan ->
          when (span.style) {
            Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
            Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
            Typeface.BOLD_ITALIC ->
                addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                    start,
                    end)
          }
      is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
      is ForegroundColorSpan -> addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
    }
  }

  toAnnotatedString()
}
