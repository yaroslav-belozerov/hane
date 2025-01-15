package com.hane.ui.screen.details

import com.hane.domain.Article
import com.hane.domain.Comment
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class DetailsScreenState(
  val article: Article? = null,
  val isLoading: Boolean = true,
  val comments: PersistentList<Comment> = persistentListOf(),
  val error: Boolean = false
)
