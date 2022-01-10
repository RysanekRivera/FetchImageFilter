package com.rysanek.fetchimagefilter.presentation.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.request.RequestOptions
import com.rysanek.fetchimagefilter.domain.usecases.UploadPicture
import com.rysanek.fetchimagefilter.domain.utils.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FilterImagesViewModel @Inject constructor(
    private val glide: RequestManager,
    private val uploadPicture: UploadPicture
) : ViewModel() {
    
    val uploadState: LiveData<UploadState> = uploadPicture.uploadState
    private fun postUploadState(state: UploadState) = uploadPicture.postUploadState(state)
    
    fun loadImagesToView(uri: String, imageView: ImageView) = viewModelScope.launch(Dispatchers.Main) {
        glide.load(uri).into(imageView)
    }
    
    fun filterAndLoadImagesToView(uri: String, imageView: ImageView, transformation: Transformation<Bitmap>) {
        filterAndLoadImage(uri, imageView, transformation)
    }
    
    private fun <T: Transformation<Bitmap>> filterAndLoadImage(imageUri: String, imageView: ImageView, transformation: T) = viewModelScope.launch(Dispatchers.IO) {
        val image = glide.load(imageUri).apply(RequestOptions.bitmapTransform(transformation)).submit().get()
        
        withContext(Dispatchers.Main) {
            glide.load(image).into(imageView)
        }
    }
    
    fun saveAndUploadImage(picture: Drawable, imageUrl: String) = viewModelScope.launch(Dispatchers.IO){
        
        postUploadState(UploadState.Uploading)
        
        try {
            val imageUri = uploadPicture.saveImageReturnUri(picture)
            imageUri?.let { uploadPicture.uploadImage(imageUri, imageUrl) }
        } catch (e: Exception) {
            postUploadState(UploadState.Error.message("Error Uploading: ${e.message}"))
        }
    }
}