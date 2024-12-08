package com.example.moviebuffs.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val currentMovie: MoviePhoto?,
    val movieList: List<MoviePhoto> = emptyList(),
    val isShowingListPage: Boolean = true

)

class MovieBuffsViewModel : ViewModel() {
    var moviebuffsUiState: MovieBuffsUiState by mutableStateOf(MovieBuffsUiState.Loading)
        private set

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

    fun navigateToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }

    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
    }

    fun getMoviePhotos() {
        viewModelScope.launch {
            moviebuffsUiState = MovieBuffsUiState.Loading
            try {
                // Update the state with the fetched data
                moviebuffsUiState =
                    MovieBuffsUiState.Success(MovieBuffsApi.retrofitService.getPhotos())
            } catch (e: IOException) {
                moviebuffsUiState = MovieBuffsUiState.Error
            }
        }
    }
}