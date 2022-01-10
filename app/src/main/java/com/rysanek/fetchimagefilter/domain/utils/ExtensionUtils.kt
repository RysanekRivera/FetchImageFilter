package com.rysanek.fetchimagefilter.domain.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.snackbar.Snackbar
import com.rysanek.fetchimagefilter.R

/**
 * Makes the application fullscreen, handles displays with cutouts and hides the status bar.
 * */
fun Window.fullScreen() {
    
    WindowCompat.setDecorFitsSystemWindows(this, true)

    // handling devices with cutouts
    attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    
    WindowInsetsControllerCompat(this, decorView).apply {
        
        // hides the status bar and makes it transient by swipe
        hide(WindowInsetsCompat.Type.statusBars())
        
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

/**
 * Makes the application fullscreen, sets the the theme and background for the activity.
 * */
fun Activity.fullScreenMode(){
    window.fullScreen()
    
    setTheme(R.style.Theme_FetchImageFilter)

    window.setBackgroundDrawableResource(R.color.white)
}

/**
 * Checks the device has internet connection.
 * @return [Boolean] - Whether or not there is an active connection.
 * */
fun Context.hasInternetConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val network = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    
    return when {
        network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

/**
 * Hides a [View]
 * */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Shows a [View]
 * */
fun View.show() {
    this.visibility = View.VISIBLE
}