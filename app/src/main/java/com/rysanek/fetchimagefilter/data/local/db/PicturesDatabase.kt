package com.rysanek.fetchimagefilter.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity

@Database(
    entities = [PictureEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PicturesDatabase: RoomDatabase(){
    
    abstract val picturesDao: PicturesDAO
}