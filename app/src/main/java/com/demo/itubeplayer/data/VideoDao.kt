package com.demo.itubeplayer.data

import androidx.room.*

@Dao
interface VideoDao {

    @Insert
    suspend fun insert(video: VideoEntity)

    @Query("SELECT * FROM videos")
    suspend fun getAllVideos(): List<VideoEntity>

    @Delete
    suspend fun delete(video: VideoEntity)
}
