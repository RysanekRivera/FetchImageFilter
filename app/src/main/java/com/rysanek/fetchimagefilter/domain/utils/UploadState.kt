package com.rysanek.fetchimagefilter.domain.utils

sealed interface UploadState {
    
    object Idle : UploadState
    object Uploading: UploadState
    object Finished: UploadState {
        var message: String? = null
    
        fun message(message: String?): UploadState {
            this.message = message
            return this
        }
    }
    
    object Error: UploadState {
        var message: String? = null
        
        fun message(message: String?): UploadState {
            this.message = message
            return this
        }
    }
}