package com.rysanek.fetchimagefilter.domain.usecases

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class LoadImagesToView @Inject constructor(
    private val glide: RequestManager
) {
    
    fun load(uri: String, imageView: ImageView){
        glide.load(uri).into(imageView)
    }

}