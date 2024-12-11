package com.example.moviebuffs.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MovieBuffsApp(
    windowSize: WindowWidthSizeClass
) {
    val movieBuffsViewModel: MovieBuffsViewModel = viewModel()
    val movieBuffsUiState by movieBuffsViewModel.movieBuffsUiState.collectAsState()
    val uiState by movieBuffsViewModel.uiState.collectAsState()

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            HomeScreen(
                movieBuffsUiState = movieBuffsUiState,
                uiState = uiState,
                retryAction = movieBuffsViewModel::getMoviePhotos,
                onClick = { movie -> movieBuffsViewModel.navigateToDetailPage(movie) },
                navigateBackToListPage = { movieBuffsViewModel.navigateBackToListPage() }
            )
        }
        WindowWidthSizeClass.Medium -> {
            HomeScreen(
                movieBuffsUiState = movieBuffsUiState,
                uiState = uiState,
                retryAction = movieBuffsViewModel::getMoviePhotos,
                onClick = { movie -> movieBuffsViewModel.navigateToDetailPage(movie) },
                navigateBackToListPage = { movieBuffsViewModel.navigateBackToListPage() }
            )
        }
        WindowWidthSizeClass.Expanded -> {
            MoviesListAndDetails(
                movieBuffsUiState = movieBuffsUiState,
                uiState = uiState,
                retryAction = movieBuffsViewModel::getMoviePhotos,
                onClick = { movie -> movieBuffsViewModel.navigateToDetailPage(movie) },
                navigateBackToListPage = { movieBuffsViewModel.navigateBackToListPage() }
            )
        }
    }
}







