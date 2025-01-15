package com.hane.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hane.domain.ArticleRepository
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val repo: ArticleRepository) : ViewModel() {
  private val _state = MutableStateFlow(MainScreenState())
  val state = _state.asStateFlow()

  fun fetchArticleList(forceRefresh: Boolean = false) {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }
      repo.topStories(forceRefresh = forceRefresh).collect { stories ->
        _state.update { state ->
          state.copy(articles = state.articles + stories.map { it.id to it })
        }
      }
      _state.update { it.copy(isLoading = false) }
    }
  }
}
