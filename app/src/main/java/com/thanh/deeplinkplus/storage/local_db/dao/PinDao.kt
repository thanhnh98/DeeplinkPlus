package com.thanh.deeplinkplus.storage.local_db.dao

import androidx.room.*
import com.thanh.deeplinkplus.model.PinModel
import com.thanh.deeplinkplus.model.UrlModel
import io.reactivex.Single

@Dao
interface PinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUrl(pin: PinModel)

    @Query("delete from PIN where id = :id")
    fun removeUrl(id: Int): Single<Int>

}