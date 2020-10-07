package com.thanh.deeplinkplus.storage.local_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.thanh.deeplinkplus.model.UrlModel
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UrlDao {
    @Insert(onConflict = REPLACE)
    fun insertUrl(url: UrlModel): Single<Unit>

    @Query("select * from URL")
    fun getListUrl(): Flowable<List<UrlModel>>

    @Query("delete from URL where id = :id")
    fun removeUrl(id: Int): Single<Int>

    @Query("select * from URL where id = :id")
    fun getUrlById(id: Int): Single<UrlModel>

    @Query("delete from URL")
    fun removeAllLocalData(): Single<Int>
}