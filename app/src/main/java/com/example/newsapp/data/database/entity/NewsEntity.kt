package com.example.newsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.domain.model.INews

@Entity
data class NewsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val img: String,
    val localImg: String,
    val newsDate: String,
    val annotation: String,
    val idResource: Int,
    val type: Int,
    val newsDateUts: String,
    val mobileUrl: String,
    override var hidden: Boolean?
) : INews