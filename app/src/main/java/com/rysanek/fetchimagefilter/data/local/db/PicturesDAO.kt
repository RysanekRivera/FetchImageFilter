package com.rysanek.fetchimagefilter.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rysanek.fetchimagefilter.data.local.entities.PictureEntity
import com.rysanek.fetchimagefilter.domain.utils.Constants.PICTURES_TABLE


@Dao
interface PicturesDAO {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicturesListIntoDb(picture: PictureEntity)
    
    @Query("DELETE FROM $PICTURES_TABLE")
    suspend fun deleteAllPicturesFromDb()
    
    @Query("SELECT * FROM $PICTURES_TABLE")
    fun getPicturesListFromDbLiveData(): LiveData<List<PictureEntity>>
 
}