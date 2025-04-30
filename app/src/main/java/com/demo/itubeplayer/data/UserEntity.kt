package com.demo.itubeplayer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val username: String,
    val password: String,
    val fullName: String
)
