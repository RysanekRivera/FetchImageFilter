package com.rysanek.fetchimagefilter.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.rysanek.fetchimagefilter.domain.repositories.FetchImageFilterRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadFromDbViewModel @Inject constructor(
    private val repository: FetchImageFilterRepositoryImpl
): ViewModel() {
    
    fun loadPicturesListFromDb() = repository.getAllListItemsFromDbLiveData()
}