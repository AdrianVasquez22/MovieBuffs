package com.example.moviebuffs.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MovieBuffsApp(
    windowSize: WindowWidthSizeClass
) {
    val movieBuffsViewModel: MovieBuffsViewModel = viewModel()
    val uiState by movieBuffsViewModel.uiState.collectAsState()

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            HomeScreen(
                movieBuffsUiState = movieBuffsViewModel.moviebuffsUiState,
                retryAction = movieBuffsViewModel::getMoviePhotos,
                onClick = { movie ->
                    movieBuffsViewModel.updateCurrentMovie(movie)
                    movieBuffsViewModel.navigateToDetailPage()
                }
            )
        }
        WindowWidthSizeClass.Medium -> {
            MovieListAndDetails(
                uiState = uiState,
                updateCurrentMovie = movieBuffsViewModel::updateCurrentMovie,
                navigateToDetailPage = movieBuffsViewModel::navigateToDetailPage,
                navigateToListPage = movieBuffsViewModel::navigateToListPage
            )
        }
        WindowWidthSizeClass.Expanded -> {
            MovieListAndDetails(
                uiState = uiState,
                updateCurrentMovie = movieBuffsViewModel::updateCurrentMovie,
                navigateToDetailPage = movieBuffsViewModel::navigateToDetailPage,
                navigateToListPage = movieBuffsViewModel::navigateToListPage
            )
        }
        else -> {
            HomeScreen(
                movieBuffsUiState = movieBuffsViewModel.moviebuffsUiState,
                retryAction = movieBuffsViewModel::getMoviePhotos,
                onClick = { movie ->
                    movieBuffsViewModel.updateCurrentMovie(movie)
                    movieBuffsViewModel.navigateToDetailPage()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBuffsTopAppBar(
    title: String,
    onBackPressed: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        modifier = modifier
    )
}

