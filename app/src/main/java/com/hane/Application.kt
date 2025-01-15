package com.hane

import android.app.Application
import androidx.room.Room
import com.hane.data.ArticleRepositoryImpl
import com.hane.data.local.ArticleDao
import com.hane.data.local.ArticleDb
import com.hane.data.remote.HackerNewsApi
import com.hane.domain.ArticleRepository
import com.hane.ui.screen.details.DetailsViewModel
import com.hane.ui.screen.main.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class Application: Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@Application)
      modules(Module)
    }
  }

  companion object {
    val Module = module {
      single<HttpClient> {
        HttpClient(OkHttpEngine(OkHttpConfig())) {
          install(ContentNegotiation) {
            json(Json {
              prettyPrint = true
              ignoreUnknownKeys = true
              isLenient = true
            })
          }
          defaultRequest {
            io.ktor.http.headers {
              append("Content-Type", "application/json")
              append("accept", "application/json")
              append("Accept-Encoding", "gzip")
            }
          }
        }
      }
      single {
        HackerNewsApi(get())
      }
      single<ArticleDao> {
        Room.databaseBuilder(androidContext(), ArticleDb::class.java, "article.db").build().dao()
      }
      factory<ArticleRepository> {
        ArticleRepositoryImpl(get(), get(), get())
      }
      viewModelOf(::MainViewModel)
      viewModelOf(::DetailsViewModel)
    }
  }
}