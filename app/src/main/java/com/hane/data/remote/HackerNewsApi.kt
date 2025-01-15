package com.hane.data.remote

import com.hane.C
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class HackerNewsApi(private val client: HttpClient) {
  suspend fun getTopIds(): List<Long> = client.get("${C.Net.BaseUrl}topstories.json").body()

  suspend fun getArticle(id: Long): ArticleDto =
      client.get("${C.Net.BaseUrl}item/$id.json").body()

  suspend fun getComment(id: Long): CommentDto = client.get("${C.Net.BaseUrl}item/$id.json").body()
}
