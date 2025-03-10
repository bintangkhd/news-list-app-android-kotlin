package com.bintangkhd.newslist.di

import android.content.Context
import androidx.room.Room
import com.bintangkhd.newslist.BuildConfig
import com.bintangkhd.newslist.data.service.local.NewsHistoryDao
import com.bintangkhd.newslist.data.service.local.NewsHistoryDatabase
import com.bintangkhd.newslist.data.service.remote.ApiService
import com.bintangkhd.newslist.repository.NewsHistoryRepository
import com.bintangkhd.newslist.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService): NewsRepository {
        return NewsRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsHistoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NewsHistoryDatabase::class.java,
            "news_history_db"
        ).build()
    }

    @Provides
    fun provideNewsHistoryDao(database: NewsHistoryDatabase): NewsHistoryDao {
        return database.newsHistoryDao()
    }

    @Provides
    @Singleton
    fun provideNewsHistoryRepository(newsHistoryDao: NewsHistoryDao): NewsHistoryRepository {
        return NewsHistoryRepository(newsHistoryDao)
    }

}