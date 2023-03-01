package com.example.giphyviewer.di

import com.example.giphyviewer.AppRepository
import com.example.giphyviewer.models.GiphyListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesAppRepository(repo: AppRepository): GiphyListRepository
}