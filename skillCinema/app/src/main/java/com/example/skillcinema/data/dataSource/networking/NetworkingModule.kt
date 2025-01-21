package com.example.skillcinema.data.dataSource.networking

import com.example.skillcinema.data.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun baseUrl(): String {
        return BASE_URL
    }

    @Singleton
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun movieApi(): MovieApi {
        return retrofit().create()
    }

    @Singleton
    @Provides
    fun providesRepository(movieApi: MovieApi) = MovieRepository(movieApi)

    private const val BASE_URL = "https://kinopoiskapiunofficial.tech"

}