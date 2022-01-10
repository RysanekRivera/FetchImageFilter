package com.rysanek.fetchimagefilter.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.fetchimagefilter.domain.usecases.FetchPicturesAndSaveToDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchImagesViewModel @Inject constructor(
    private val fetchPicturesAndSaveToDB: FetchPicturesAndSaveToDB
): ViewModel() {
    
    val downloadState = fetchPicturesAndSaveToDB.downloadState
    
    fun fetchPicturesAndSaveToDB() = viewModelScope.launch(Dispatchers.IO) {
        fetchPicturesAndSaveToDB.start()
    }
    
}