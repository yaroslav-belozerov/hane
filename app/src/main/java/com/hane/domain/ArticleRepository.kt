package com.hane.domain

import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
  suspend fun getArticle(id: Long, forceRefresh: Boolean = false): Result<Article>
  suspend fun getComment(id: Long): Result<Comment>
  fun topStories(batchSize: Int = 5, forceRefresh: Boolean = false): Flow<List<Article>>
}
