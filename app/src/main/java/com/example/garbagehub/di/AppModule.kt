package com.example.garbagehub.di

import android.content.Context
import com.example.garbagehub.data.api.GarbageHubApiService
import com.example.garbagehub.data.database.GarbageHubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GarbageHubDatabase {
        return GarbageHubDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBinDao(database: GarbageHubDatabase) = database.binDao()

    @Provides
    @Singleton
    fun provideReportDao(database: GarbageHubDatabase) = database.reportDao()

    @Provides
    @Singleton
    fun provideMaintenanceDao(database: GarbageHubDatabase) = database.maintenanceDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GarbageHubApiService {
        return retrofit.create(GarbageHubApiService::class.java)
    }

    private const val BASE_URL = "https://api.garbagehub.com/" // Replace with actual API URL
}
