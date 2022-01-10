package com.rysanek.fetchimagefilter.domain.usecases

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rysanek.fetchimagefilter.domain.repositories.FetchImageFilterRepositoryImpl
import com.rysanek.fetchimagefilter.domain.utils.Constants.APP_ID
import com.rysanek.fetchimagefilter.domain.utils.Constants.URL_PREFIX
import com.rysanek.fetchimagefilter.domain.utils.UploadState
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadPicture @Inject constructor(
    private val repository: FetchImageFilterRepositoryImpl
) {
    private val _uploadState = MutableLiveData<UploadState>(UploadState.Idle)
    val uploadState: LiveData<UploadState> = _uploadState
    fun postUploadState(state: UploadState) { _uploadState.postValue(state) }
    
    suspend fun uploadImage(
        imageUri: String, imageUrl: String
    ) {
        val file = File(imageUri)
        
        val filePart = MultipartBody.Part.createFormData(
            APP_ID, imageUrl, file.asRequestBody("image/jpg".toMediaType())
        )
        
        fetchUpdateUrlAndPostImage(imageUrl, imageUri, filePart)
    }
    
    fun saveImageReturnUri(image: Drawable): String? {
        return repository.saveImageToInternalStorageReturnUri(image.toBitmap())
    }
    
    private fun deletePictureFromInternalStorage(imageUri: String){
        repository.deleteImagesFromInternalStorage(imageUri)
    }
    
    private suspend fun fetchUpdateUrlAndPostImage(originalUrl: String, imageUri: String, file: MultipartBody.Part) =
        repository.fetchUploadUrl()
            .catch { e -> Log.e("UploadPictureF", "Error: ${e.message}") }
            .filter { response -> response.isSuccessful && response.body() != null }
            .map { response ->
                
                val uploadUrl = response.body()?.url?.removePrefix(URL_PREFIX) ?: ""
                
                uploadImageToServer(uploadUrl, APP_ID, originalUrl, file)
                
            }.onCompletion {
                
                deletePictureFromInternalStorage(imageUri)
                
            }.collect()
    
    private suspend fun uploadImageToServer(
        uploadUrl: String, appId: String, originalUrl: String, file: MultipartBody.Part
    ) = repository.uploadImageToServer(uploadUrl, appId, originalUrl, file)
        .catch { e ->
            
            Log.e("UploadPicture", "Error: ${e.message}")
            postUploadState(UploadState.Error.message("Error Uploading: ${e.message}"))
            
        }
        .filter { response -> response.isSuccessful && response.body() != null }
        .map { response ->
            
            postUploadState(UploadState.Finished.message("upload successful: ${response.isSuccessful}"))
            
        }.collect()
}