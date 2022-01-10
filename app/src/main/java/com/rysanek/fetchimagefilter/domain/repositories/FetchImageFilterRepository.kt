package com.rysanek.fetchimagefilter.domain.repositories

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity
import com.rysanek.fetchimagefilter.data.remote.dtos.PictureDTO
import com.rysanek.fetchimagefilter.data.remote.dtos.UploadUrlDTO
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface FetchImageFilterRepository {
    suspend fun fetchRemotePicturesList(): Flow<Response<List<PictureDTO>>>
    
    suspend fun insertPictureIntoDb(pictureEntity: PictureEntity)
    
    suspend fun deleteAllItemsFromDb()
    
    fun getAllListItemsFromDbLiveData(): LiveData<List<PictureEntity>>
    
    fun hasInternetConnection(context: Context): Boolean
    
    fun getRemoteContentLength(): Long
    
    fun downloadImageAsBitmap(imageUrl: String?): Bitmap?
    
    fun saveImageToInternalStorageReturnUri(bitmap: Bitmap?): String?
    
    fun deleteImagesFromInternalStorage(imageUri: String)
    
    suspend fun fetchUploadUrl(): Flow<Response<UploadUrlDTO>>
    
    suspend fun uploadImageToServer(url: String, appId: String, originalUrl: String, file: MultipartBody.Part): Flow<Response<ResponseBody>>
}