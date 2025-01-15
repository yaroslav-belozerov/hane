package com.hane.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hane.R

private val normalFontFamily = FontFamily(
  Font(R.font.inter_black, FontWeight.Black, FontStyle.Normal),
  Font(R.font.inter_blackitalic, FontWeight.Black, FontStyle.Italic),
  Font(R.font.inter_bold, FontWeight.Bold, FontStyle.Normal),
  Font(R.font.inter_bolditalic, FontWeight.Bold, FontStyle.Italic),
  Font(R.font.inter_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
  Font(R.font.inter_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
  Font(R.font.inter_extralight, FontWeight.ExtraLight, FontStyle.Normal),
  Font(R.font.inter_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
  Font(R.font.inter_regular, FontWeight.Normal, FontStyle.Normal),
  Font(R.font.inter_italic, FontWeight.Normal, FontStyle.Italic),
  Font(R.font.inter_light, FontWeight.Light, FontStyle.Normal),
  Font(R.font.inter_lightitalic, FontWeight.Light, FontStyle.Italic),
  Font(R.font.inter_medium, FontWeight.Medium, FontStyle.Normal),
  Font(R.font.inter_mediumitalic, FontWeight.Medium, FontStyle.Italic),
  Font(R.font.inter_semibold, FontWeight.SemiBold, FontStyle.Normal),
  Font(R.font.inter_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
  Font(R.font.inter_thin, FontWeight.Thin, FontStyle.Normal),
  Font(R.font.inter_thinitalic, FontWeight.Thin, FontStyle.Italic),
)
private val displayFontFamily = FontFamily(Font(R.font.dela_gothic_one))

val Typography = Typography(
  bodyLarge = TextStyle(
    fontFamily = normalFontFamily,
    fontSize = 20.sp,
  ),
  bodyMedium = TextStyle(
    fontFamily = normalFontFamily,
    fontSize = 16.sp,
  ),
  bodySmall = TextStyle(
    fontFamily = normalFontFamily,
    fontSize = 12.sp,
  ),
  displaySmall = TextStyle(
    fontFamily = displayFontFamily
  ),
  displayMedium = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 24.sp
  ),
  displayLarge = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 32.sp,
    lineHeight = 32.sp
  ),
)