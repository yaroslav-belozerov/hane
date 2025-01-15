package com.hane.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hane.C
import com.hane.domain.Article
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.DetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Destination<RootGraph>(start = true)
@Composable
fun MainScreen(navigator: DestinationsNavigator, viewModel: MainViewModel = koinViewModel()) {
  val state by viewModel.state.collectAsState()
  var isInfoShown by remember { mutableStateOf(false) }
  LaunchedEffect(null) { viewModel.fetchArticleList() }

  if (isInfoShown) {
    Dialog(onDismissRequest = { isInfoShown = false }) {
      ElevatedCard {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp)) {
          GlideImage(
            "https://techcrunch.com/wp-content/uploads/2013/05/hacker-news1.jpg",
            contentDescription = "HackerNews Logo",
            modifier = Modifier.clip(MaterialTheme.shapes.medium))
          val uriHandler = LocalUriHandler.current
          Card(onClick = { uriHandler.openUri("https://github.com/HackerNews/API") }) {
            Text(
              "https://github.com/HackerNews/API",
              color = MaterialTheme.colorScheme.tertiary,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(8.dp).fillMaxWidth())
          }
        }
      }
    }
  }

  PullToRefreshBox(state.isLoading, onRefresh = { viewModel.fetchArticleList(true) }) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
          item {
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  Text(
                      "hane",
                      modifier = Modifier.weight(1f),
                      fontSize = 24.sp,
                      fontFamily = MaterialTheme.typography.displaySmall.fontFamily)
                  IconButton(onClick = { isInfoShown = true }) { Icon(Icons.Default.Info, "info") }
                }
          }
          items(state.articles.keys.toList(), key = { it }) { id ->
            ArticleCard(
                state.articles[id]!!,
                modifier = Modifier.animateItem(),
                onClick = { navigator.navigate(DetailsScreenDestination(id)) })
          }
        }
  }
}

@Composable
fun ArticleCard(article: Article, modifier: Modifier, onClick: () -> Unit) {
  Card(modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp), onClick = onClick) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically) {
            Text(
                article.time,
                style = MaterialTheme.typography.bodyMedium,
                color = C.UI.dimOnBackground,
                modifier = Modifier.weight(1f))
          }
      Text(article.title, style = MaterialTheme.typography.bodyLarge)
      Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically) {
            Text(article.by, style = MaterialTheme.typography.bodyMedium, color = C.UI.dimPrimary)
            Text(
                article.score,
                style = MaterialTheme.typography.bodyMedium,
                color = C.UI.dimOnBackground)
          }
    }
  }
}
