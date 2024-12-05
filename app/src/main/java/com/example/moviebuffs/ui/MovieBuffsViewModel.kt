package com.example.moviebuffs.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffs.network.MovieBuffsApi
import com.example.moviebuffs.network.MoviePhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieBuffsUiState {
    data class Success(val photos: MoviePhoto) : MovieBuffsUiState
    object Error : MovieBuffsUiState
    object Loading : MovieBuffsUiState
}

class MovieBuffsViewModel : ViewModel() {
    var moviebuffsUiState: MovieBuffsUiState by mutableStateOf(MovieBuffsUiState.Loading)
        private set

    init {
        getMoviePhotos()
    }

    fun getMoviePhotos() {
        viewModelScope.launch {
            try {
                MovieBuffsUiState.Success(MovieBuffsApi.retrofitService.getPhotos()[0])
            } catch (e: IOException) {
                moviebuffsUiState = MovieBuffsUiState.Error
            }
        }
    }
}