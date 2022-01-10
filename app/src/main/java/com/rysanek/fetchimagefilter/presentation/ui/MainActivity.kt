package com.rysanek.fetchimagefilter.presentation.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.rysanek.fetchimagefilter.R
import com.rysanek.fetchimagefilter.databinding.ActivityMainBinding
import com.rysanek.fetchimagefilter.domain.utils.fullScreenMode
import com.rysanek.fetchimagefilter.domain.workers.initializeFetchWork
import com.rysanek.fetchimagefilter.presentation.viewmodels.FetchImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        fullScreenMode()
        
        initializeFetchWork()
        
        super.onCreate(savedInstanceState)
        
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
    
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        
        setTheme(R.style.Theme_FetchImageFilter)
    }
    
}