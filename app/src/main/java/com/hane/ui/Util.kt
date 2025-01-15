package com.hane.ui

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode


fun Modifier.shimmerBackground(shape: Shape = RectangleShape): Modifier = composed {
  val transition = rememberInfiniteTransition(label = "")

  val translateAnimation by
  transition.animateFloat(
    initialValue = 0f,
    targetValue = 400f,
    animationSpec =
    infiniteRepeatable(
      tween(durationMillis = 2000, easing = EaseInOut), RepeatMode.Restart),
    label = "",
  )
  val shimmerColors =
    listOf(
      MaterialTheme.colorScheme.onBackground.copy(alpha = 0.0f),
      MaterialTheme.colorScheme.onBackground.copy(alpha = 0.0f),
      MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
    )
  val brush =
    Brush.linearGradient(
      colors = shimmerColors,
      start = Offset(translateAnimation, translateAnimation),
      end = Offset(translateAnimation + 200f, translateAnimation + 200f),
      tileMode = TileMode.Mirror,
    )
  return@composed this.then(background(brush, shape))
}
