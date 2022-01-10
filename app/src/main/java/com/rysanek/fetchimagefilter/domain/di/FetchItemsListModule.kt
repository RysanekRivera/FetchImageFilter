package com.rysanek.fetchimagefilter.domain.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.RequestManager
import com.rysanek.fetchimagefilter.data.local.db.PicturesDAO
import com.rysanek.fetchimagefilter.data.local.db.PicturesDatabase
import com.rysanek.fetchimagefilter.domain.utils.Constants.PICTURES_DATABASE
import com.rysanek.fetchimagefilter.data.remote.apis.FetchImageFilterAPI
import com.rysanek.fetchimagefilter.domain.glide.GlideApp
import com.rysanek.fetchimagefilter.domain.usecases.FetchPicturesAndSaveToDB
import com.rysanek.fetchimagefilter.domain.utils.Constants.BASE_URL
import com.rysanek.fetchimagefilter.presentation.viewmodels.FetchImagesViewModel
import com.rysanek.fetchimagefilter.presentation.viewmodels.FilterImagesViewModel
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
object FetchItemsListModule {
    
    @Provides
    @Singleton
    fun provideItemsListDatabase(
        application: Application
    ) = Room.databaseBuilder(
        application,
        PicturesDatabase::class.java,
        PICTURES_DATABASE
    )
    .build()
    
    @Provides
    @Singleton
    fun provideFetchItemsListAPI(): FetchImageFilterAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient()
            .newBuilder()
            .addNetworkInterceptor(HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BASIC) })
            .build()
        )
        .build()
        .create(FetchImageFilterAPI::class.java)
    
    @Singleton
    @Provides
    fun provideFetchItemsListDAO(
        db: PicturesDatabase
    ): PicturesDAO = db.picturesDao
    
    @Singleton
    @Provides
    fun provideContext(
        @ApplicationContext context: Context
    ) = context
    
    @Singleton
    @Provides
    fun provideGlideInstance(context: Context): RequestManager = GlideApp.with(context)
    
    @Singleton
    @Provides
    fun provideFetchImagesViewModel(
        fetchPicturesAndSaveToDB: FetchPicturesAndSaveToDB
    ) = FetchImagesViewModel(fetchPicturesAndSaveToDB)
    
}