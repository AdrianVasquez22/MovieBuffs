package com.example.moviebuffs.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePhoto(
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)
