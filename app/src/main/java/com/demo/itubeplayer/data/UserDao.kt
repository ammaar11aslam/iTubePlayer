package com.demo.itubeplayer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): UserEntity?
}
