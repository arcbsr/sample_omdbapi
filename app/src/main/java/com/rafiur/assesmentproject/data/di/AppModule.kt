package com.rafiur.assesmentproject.data.di

import com.cube.cubeacademy.lib.repository.Repository
import com.rafiur.assesmentproject.data.datasource.DataSource
import com.rafiur.assesmentproject.data.repository.RepositoryImpl
import com.rafiur.assesmentproject.domain.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApi(): ApiService = OkHttpClient().newBuilder()
//        .apply {
//            addInterceptor(AuthTokenInterceptor())
//        }
        .build().let {
            Retrofit.Builder()
                .client(it)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.omdbapi.com/").build().create(ApiService::class.java)
        }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            // Add any additional configurations such as interceptors or network settings
            .build()
    }

    @Singleton
    @Provides
    fun provideRepositoryImpl(dataSource: DataSource): Repository = RepositoryImpl(dataSource)
}