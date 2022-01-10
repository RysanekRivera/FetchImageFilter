package com.rysanek.fetchimagefilter.presentation.viewmodels

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.fetchimagefilter.domain.usecases.LoadImagesToView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadImagesViewModel @Inject constructor(
    private val loadImagesToView: LoadImagesToView
): ViewModel() {
    
    fun loadImagesToView(url: String, imageView: ImageView) = viewModelScope.launch(Dispatchers.Main) {
        loadImagesToView.load(url, imageView)
    }
}