package com.hane.ui.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.hane.C
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.min

object DetailsScreenTransition : DestinationStyle.Animated() {
  override val enterTransition:
      AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    get() = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(300)) }

  override val exitTransition:
      AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    get() = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(300)) }
}

@Destination<RootGraph>(style = DetailsScreenTransition::class)
@Composable
fun DetailsScreen(
    navigator: DestinationsNavigator,
    id: Long,
    viewModel: DetailsViewModel = koinViewModel()
) {
  LaunchedEffect(null) { viewModel.loadDetails(id) }
  val state by viewModel.state.collectAsState()
  LazyColumn(
      modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navigator.navigateUp() }) {
              Icon(Icons.AutoMirrored.Default.ArrowBack, "back")
            }
          }
        }
        item {
          state.article?.let {
            ArticleContent(it.by, it.time, it.score, it.title)
          }
        }
        if (state.error) {
          item { Box(modifier = Modifier.fillParentMaxWidth().height(128.dp).clip(MaterialTheme.shapes.medium).background(MaterialTheme.colorScheme.errorContainer).clickable { viewModel.loadDetails(state.article!!.id) }, contentAlignment = Alignment.Center) { Icon(Icons.Default.Refresh, "refresh") } }
        }
        item { UrlCard(state.article?.url) }
        if (state.isLoading && !state.error) {
          item { LinearProgressIndicator(Modifier.fillMaxWidth().padding(16.dp)) }
        }
        items(state.comments, key = { it.id }) {
          Column(modifier = Modifier.animateItem()) { CommentCard(it.by, it.text) }
        }
      }
}

@Composable
fun CommentCard(author: String, text: AnnotatedString) {
  var isExpanded by remember { mutableStateOf(false) }
  val isExpandedProgress by animateFloatAsState(targetValue = if (isExpanded) 0f else 180f)
  var isButtonVisible by rememberSaveable { mutableStateOf(false) }
  var didCalc by remember { mutableStateOf(false) }
  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(
        author,
        modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 8.dp).weight(1f),
        style = MaterialTheme.typography.bodyLarge)
  }
  ElevatedCard {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        text,
        modifier = Modifier.padding(horizontal = 12.dp).padding(top = 12.dp).height(if (isExpanded) Dp.Unspecified else 64.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = C.UI.dimOnBackground,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { res ->
          if (!didCalc) {
            didCalc = true
            isButtonVisible =
              res.getLineEnd(res.lineCount - 1, visibleEnd = true) != text.length
          }
        })
      if (isButtonVisible)
        Surface(
          modifier = Modifier.fillMaxWidth().padding(top = if (isExpanded) 8.dp else 0.dp).height(36.dp),
          shape = MaterialTheme.shapes.medium,
          color = Color.Transparent,
          onClick = { isExpanded = !isExpanded }) {
          Icon(
            Icons.Default.KeyboardArrowUp,
            contentDescription = "show more/less",
            tint = C.UI.dimOnBackground, modifier = Modifier.graphicsLayer { rotationZ = isExpandedProgress })
        } else Spacer(Modifier.height(8.dp))
    }
  }
}

@Composable
fun ArticleContent(
    author: String,
    time: String,
    score: String,
    title: String
) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Text(
              author,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.primary)
          Text(time, style = MaterialTheme.typography.bodyMedium, color = C.UI.dimOnBackground)
          Text(score, style = MaterialTheme.typography.bodyMedium, color = C.UI.dimOnBackground)
        }
    Text(title, style = MaterialTheme.typography.displayLarge)
  }
}

@Composable
fun UrlCard(url: String?) {
  val uriHandle = LocalUriHandler.current
  AnimatedVisibility(url != null, enter = expandVertically()) {
    ElevatedCard(onClick = { uriHandle.openUri(url!!) }) {
      Row(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Search,
                contentDescription = "open in browser",
                tint = MaterialTheme.colorScheme.tertiary)
            Text(
                url!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary)
          }
    }
  }
}
