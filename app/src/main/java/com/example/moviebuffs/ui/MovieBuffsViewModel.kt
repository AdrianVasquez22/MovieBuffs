package com.example.moviebuffs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffs.network.MovieBuffsApi
import com.example.moviebuffs.network.MoviePhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieBuffsUiState {
    data class Success(val photos: List<MoviePhoto>) : MovieBuffsUiState
    object Error : MovieBuffsUiState
    object Loading : MovieBuffsUiState
}

data class UiState(
    val currentMovie: MoviePhoto? = null,
    val movieList: List<MoviePhoto> = emptyList(),
    val isShowingListPage: Boolean = true
)

class MovieBuffsViewModel : ViewModel() {
    private val _movieBuffsUiState = MutableStateFlow<MovieBuffsUiState>(MovieBuffsUiState.Loading)
    val movieBuffsUiState: StateFlow<MovieBuffsUiState> = _movieBuffsUiState

    private val _uiState = MutableStateFlow(
        UiState(
            currentMovie = null,
            isShowingListPage = true
        )
    )
    val uiState: StateFlow<UiState> = _uiState

    init {
        getMoviePhotos()
    }

    fun updateCurrentMovie(selectedMovie: MoviePhoto) {
        _uiState.update {
            it.copy(currentMovie = selectedMovie)
        }
    }

    fun navigateBackToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }

    fun navigateToDetailPage(selectedMovie: MoviePhoto) {
        _uiState.update {
            it.copy(
                currentMovie = selectedMovie,
                isShowingListPage = false
            )
        }
    }

    fun getMoviePhotos() {
        viewModelScope.launch {
            _movieBuffsUiState.value = MovieBuffsUiState.Loading
            try {
                val photos = MovieBuffsApi.retrofitService.getPhotos()
                _movieBuffsUiState.value = MovieBuffsUiState.Success(photos)
            } catch (e: IOException) {
                _movieBuffsUiState.value = MovieBuffsUiState.Error
            }
        }
    }
}
