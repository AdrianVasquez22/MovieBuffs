package com.example.moviebuffs.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePhoto(
    val title: String,
    val poster: String,
    val description: String,
    @SerialName(value = "release_date")
    val date: String,
    @SerialName(value = "content_rating")
    val rating: String,
    @SerialName(value = "review_score")
    val reviewScore: String,
    @SerialName(value = "big_image")
    val imgSrc: String,
    val length: String
)
