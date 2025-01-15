package com.hane.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hane.domain.ArticleRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo: ArticleRepository): ViewModel() {
  private val _state = MutableStateFlow(DetailsScreenState())
  val state = _state.asStateFlow()

  fun loadDetails(id: Long) {
    viewModelScope.launch {
      val article = repo.getArticle(id)
      var articleValue = article.getOrNull()

      _state.update {
        DetailsScreenState(article = articleValue)
      }
      if (articleValue?.cached == true) {
        val networkArticle = repo.getArticle(id, forceRefresh = true)
        if (networkArticle.isFailure) {
          _state.update { it.copy(error = true) }
          return@launch
        }
        networkArticle.getOrNull()?.let { articleValue = it }
      }
      _state.update {
        DetailsScreenState(article = articleValue, isLoading = false)
      }
      articleValue?.kids?.take(100)?.forEach { kid ->
        _state.update {
          repo.getComment(kid).getOrNull()?.let { comm ->
            it.copy(
              comments = (it.comments + comm).toPersistentList()
            )
          } ?: it
        }
      }
    }
  }
}
