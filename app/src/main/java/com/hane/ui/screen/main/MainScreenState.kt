package com.hane.ui.screen.main

import com.hane.domain.Article
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

data class MainScreenState(
  val articles: PersistentMap<Long, Article> = persistentMapOf(),
  val isLoading: Boolean = true
)
