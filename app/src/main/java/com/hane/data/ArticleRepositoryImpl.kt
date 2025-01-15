package com.hane.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.hane.data.local.ArticleDao
import com.hane.data.local.toDomainModel
import com.hane.data.remote.HackerNewsApi
import com.hane.data.remote.toDomainModel
import com.hane.data.remote.toEntity
import com.hane.domain.Article
import com.hane.domain.ArticleRepository
import com.hane.domain.Comment
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class ArticleRepositoryImpl(private val context: Context, private val api: HackerNewsApi, private val dao: ArticleDao) :
    ArticleRepository {
  override suspend fun getArticle(id: Long, forceRefresh: Boolean): Result<Article> {
    val articleCached = dao.getArticle(id)
    return if (forceRefresh || articleCached == null) {
      withContext(Dispatchers.IO) {
        try {
          Result.success(api.getArticle(id).also { dao.upsert(it.toEntity()) }.toDomainModel())
        } catch (e: Throwable) {
          Result.failure(e)
        }
      }
    } else {
      Result.success(articleCached.toDomainModel())
    }
  }

  override suspend fun getComment(id: Long) = try {
    Result.success(api.getComment(id).toDomainModel())
  } catch (e: Throwable) {
    Result.failure(e)
  }

  override fun topStories(batchSize: Int, forceRefresh: Boolean) = flow {
      try {
        coroutineScope {
          val ids = api.getTopIds().take(100)
          ids.chunked(batchSize).forEach { batch ->
            val deferredStories = batch.map { id ->
              async { getArticle(id, forceRefresh) }
            }
            emit(deferredStories.awaitAll().mapNotNull { it.getOrNull() })
          }
        }
      } catch (_: Throwable) {
        emit(dao.getAllCachedArticles().map { it.toDomainModel() })
        withContext(Dispatchers.Main) {
          Toast.makeText(context, "\uD83C\uDF10 Network Error", Toast.LENGTH_SHORT).show()
        }
      }
  }.flowOn(Dispatchers.IO)
}
