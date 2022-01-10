package com.rysanek.fetchimagefilter.data.remote.dtos

import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity

data class PictureDTO(
    val created: String,
    val updated: String,
    val url: String,
) {
    fun toPictureEntity(imageUri: String? = null) = PictureEntity(created, updated, url, imageUri)
}