package com.rysanek.fetchimagefilter.domain.usecases

import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.RequestManager

/** Downloads an image as a Bitmap from a specified URL.
 * @param[imageUrl] [String?] URL.
 * @param [width] [Int] of pixels of image to download. Default value 500.
 * @param [height] [Int] of pixels of image to download. Default value 600.
 * @return [Bitmap] or null
 * **/
fun RequestManager.downLoadImageAsBitmap(
    imageUrl: String?,
    width: Int = 300,
    height: Int = 400
): Bitmap? {
    return if (imageUrl == null) null
    else try {
        asBitmap().load(imageUrl).override(width, height).submit().get()
    } catch (e: Exception) {
        Log.e("DownloadImageUtils", e.message ?: "Error downloading image")
        null
    }
}