package com.rysanek.fetchimagefilter.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.rysanek.fetchimagefilter.domain.utils.Constants.CACHE_INTERVAL
import com.rysanek.fetchimagefilter.domain.utils.Constants.FETCH_WORK_NAME
import com.rysanek.fetchimagefilter.presentation.viewmodels.FetchImagesViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.concurrent.TimeUnit

@HiltWorker
class FetchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val viewModel: FetchImagesViewModel
): CoroutineWorker(context, workerParameters)  {
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            viewModel.fetchPicturesAndSaveToDB()
            Result.success()
        }catch (e: Exception){
            Log.e("FetchWorker", "Error: ${e.message}")
            Result.retry()
        }
    }
}

/**
 * Starts the work of the [FetchWorker]. In this case it will start downloading
 * from the server and then it will check periodically after every [CACHE_INTERVAL]
 * if the data has changed from the server, if so it will perform the work once again.
 */
fun Context.initializeFetchWork(){
    val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val workRequest = PeriodicWorkRequestBuilder<FetchWorker>(CACHE_INTERVAL, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofMillis(WorkRequest.MIN_BACKOFF_MILLIS))
        .build()
    
    WorkManager.getInstance(this).enqueueUniquePeriodicWork(FETCH_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
}
