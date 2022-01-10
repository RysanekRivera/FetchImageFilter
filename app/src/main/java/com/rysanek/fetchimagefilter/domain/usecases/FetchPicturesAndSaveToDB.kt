package com.rysanek.fetchimagefilter.domain.usecases

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity
import com.rysanek.fetchimagefilter.data.remote.dtos.PictureDTO
import com.rysanek.fetchimagefilter.domain.repositories.FetchImageFilterRepositoryImpl
import com.rysanek.fetchimagefilter.domain.utils.Constants.LOCAL_CONTENT_LENGTH
import com.rysanek.fetchimagefilter.domain.utils.DownloadState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchPicturesAndSaveToDB @Inject constructor(
    private val repository: FetchImageFilterRepositoryImpl
) {
    private val _downloadState = MutableLiveData<DownloadState>(DownloadState.Idle)
    val downloadState: LiveData<DownloadState> = _downloadState
    
    private fun postDownloadState(state: DownloadState) { _downloadState.postValue(state) }
    
    suspend fun start() {
        
        postDownloadState(DownloadState.Checking)
        
        val remoteContentLength = repository.getRemoteContentLength()
        
        val localContentLength = repository.localContentLengthPrefs.getLong(LOCAL_CONTENT_LENGTH, 0)
        
        if (remoteContentLength > 0 && localContentLength != remoteContentLength) {
            
            postDownloadState(DownloadState.Downloading)
    
            fetchData()
    
            repository.localContentLengthPrefs.edit { putLong(LOCAL_CONTENT_LENGTH, remoteContentLength) }
        }
        
        postDownloadState(DownloadState.Idle)
    }
    
    private suspend fun fetchData() = repository.fetchRemotePicturesList()
        .catch { e ->
            Log.e("GetListItems", "Error: ${e.message}")
            postDownloadState(DownloadState.Error.message(e.message))
        }
        .filter { response ->
            response.isSuccessful && response.body()!= null
        }
        .map { response ->
            
           downloadImageAndSaveToDb(response.body()!!)
           
        }.collect()
    
    private suspend fun downloadImageAndSaveToDb(dotList: List<PictureDTO>) {
        
        dotList.forEach { dto ->
            
            val bitmap = repository.downloadImageAsBitmap(dto.url)
            
            val imageUri = repository.saveImageToInternalStorageReturnUri(bitmap)
            
            val entity = dto.toPictureEntity(imageUri)
    
            repository.insertPictureIntoDb(entity)
        }
    }
}