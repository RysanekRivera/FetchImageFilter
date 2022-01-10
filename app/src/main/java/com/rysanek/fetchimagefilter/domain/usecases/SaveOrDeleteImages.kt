package com.rysanek.fetchimagefilter.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.IOException
import java.util.*

private const val TAG = "SaveOrDeleteImages"

/** Saves the image to internal storage
 * @param bitmap [Bitmap] image.
 * @return [String] URI location of file or null.
 * **/
fun Context.saveImageReturnUri(bitmap: Bitmap?): String? {
    val fileName = UUID.randomUUID().toString()
    val isSaved = try {
        if (bitmap == null) {
            false
        } else {
            openFileOutput("${fileName}.jpg", Context.MODE_PRIVATE).use { fileOutputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) throw IOException("Couldn't save bitmap")
            }
            true
        }
    } catch (e: IOException) {
        Log.e(TAG, e.message ?: "Image not saved. Unknown error.")
        false
    }
    
    return if (isSaved) "$filesDir/$fileName.jpg" else null
}

/** Deletes all images of a kind from internal storage.
 * @param images [List] of article whose images need to be deleted.
 * @return [Boolean] whether all deletions were completed successfully.
 * **/
fun Context.deleteImagesFromInternalStorage(images: List<String>) = try {
    images.forEach { image ->
        val isImageDeleted = deleteFile(image)
        Log.d(TAG, "Image $image deleted: $isImageDeleted")
    }
    true
} catch (e: Exception) {
    Log.e(TAG, "Error Deleting An Image: ${e.message}")
    false
}