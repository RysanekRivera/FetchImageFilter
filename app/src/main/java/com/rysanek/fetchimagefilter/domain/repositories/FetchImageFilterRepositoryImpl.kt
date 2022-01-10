package com.rysanek.fetchimagefilter.domain.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.FileUtils
import android.util.Log
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.rysanek.fetchimagefilter.data.local.db.PicturesDAO
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity
import com.rysanek.fetchimagefilter.data.remote.apis.FetchImageFilterAPI
import com.rysanek.fetchimagefilter.data.remote.dtos.UploadUrlDTO
import com.rysanek.fetchimagefilter.domain.usecases.deleteImagesFromInternalStorage
import com.rysanek.fetchimagefilter.domain.usecases.downLoadImageAsBitmap
import com.rysanek.fetchimagefilter.domain.usecases.fetchContentLength
import com.rysanek.fetchimagefilter.domain.usecases.saveImageReturnUri
import com.rysanek.fetchimagefilter.domain.utils.Constants.APP_ID
import com.rysanek.fetchimagefilter.domain.utils.Constants.LOCAL_CONTENT_LENGTH
import com.rysanek.fetchimagefilter.domain.utils.Constants.REMOTE_CONTENT_URL
import com.rysanek.fetchimagefilter.domain.utils.hasInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FetchImageFilterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: FetchImageFilterAPI,
    private val dao: PicturesDAO,
    private val glide: RequestManager
): FetchImageFilterRepository {
    
    val localContentLengthPrefs: SharedPreferences = context.getSharedPreferences(LOCAL_CONTENT_LENGTH, MODE_PRIVATE)
    
    override suspend fun fetchRemotePicturesList() = flow { emit(api.fetchRemotePicturesList()) }
    
    override suspend fun insertPictureIntoDb(pictureEntity: PictureEntity) = dao.insertPicturesListIntoDb(pictureEntity)
    
    override suspend fun deleteAllItemsFromDb() = dao.deleteAllPicturesFromDb()
    
    override fun getAllListItemsFromDbLiveData() = dao.getPicturesListFromDbLiveData()
    
    override fun hasInternetConnection(context: Context) = context.hasInternetConnection()
    
    override fun getRemoteContentLength(): Long = fetchContentLength(REMOTE_CONTENT_URL)
    
    override fun downloadImageAsBitmap(imageUrl: String?): Bitmap? = glide.downLoadImageAsBitmap(imageUrl)
    
    override fun saveImageToInternalStorageReturnUri(bitmap: Bitmap?) = context.saveImageReturnUri(bitmap)
    
    override fun deleteImagesFromInternalStorage(imageUri: String) {
        val fileName = imageUri.removePrefix("${context.filesDir}/")
        context.deleteImagesFromInternalStorage(listOf(fileName))
    }
    
    override suspend fun fetchUploadUrl(): Flow<Response<UploadUrlDTO>> = flow { emit(api.fetchUploadUrl()) }
    
    override suspend fun uploadImageToServer(url: String, appId: String, originalUrl: String, file: MultipartBody.Part)= flow { emit(api.uploadImage(url, appId,originalUrl, file))  }
}