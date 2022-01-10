package com.rysanek.fetchimagefilter.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rysanek.fetchimagefilter.domain.utils.Constants.PICTURES_TABLE

@Entity(tableName = PICTURES_TABLE)
data class PictureEntity(
    val created: String,
    val updated: String,
    val url: String,
    var imageUri: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)