package com.example.moviebuffs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebuffs.R
import com.example.moviebuffs.network.MoviePhoto

@Composable
fun HomeScreen(
    movieBuffsUiState: MovieBuffsUiState,
    uiState: UiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (MoviePhoto) -> Unit,
    navigateBackToListPage: () -> Unit // Pass this as a parameter
) {
    when (movieBuffsUiState) {
        is MovieBuffsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is MovieBuffsUiState.Success -> {
            if (uiState.isShowingListPage) {
                PhotosListScreen(movieBuffsUiState.photos, modifier, onClick)
            } else {
                uiState.currentMovie?.let {
                    MoviesDetail(movie = it, onBackClick = { navigateBackToListPage() })
                }
            }
        }
        is MovieBuffsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun MoviePhotoCard(photo: MoviePhoto, modifier: Modifier = Modifier, onClick: (MoviePhoto) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(top = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(photo.imgSrc)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.movie_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 8.dp)
                    .clickable { onClick(photo) }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = photo.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = photo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star Icon",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    Text(
                        text = photo.reviewScore,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun PhotosListScreen(photos: List<MoviePhoto>, modifier: Modifier = Modifier, onClick: (MoviePhoto) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        items(photos) { photo ->
            MoviePhotoCard(photo = photo, modifier = Modifier, onClick = onClick)
        }
    }
}



@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}


@Composable
fun MoviesDetail(movie: MoviePhoto, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(movie.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Information Icon",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = movie.rating,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${movie.length} | ",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Release Date Icon",
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = movie.date,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Review Score Icon",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = movie.reviewScore,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movie.description,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun MoviesListAndDetails(
    movieBuffsUiState: MovieBuffsUiState,
    uiState: UiState,
    retryAction: () -> Unit,
    onClick: (MoviePhoto) -> Unit,
    navigateBackToListPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            when (movieBuffsUiState) {
                is MovieBuffsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is MovieBuffsUiState.Success -> PhotosListScreen(
                    photos = movieBuffsUiState.photos,
                    modifier = modifier,
                    onClick = onClick
                )
                is MovieBuffsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
            }
        }

        Column(
            modifier = Modifier
                .weight(3f)
                .padding(4.dp)
        ) {
            uiState.currentMovie?.let { movie ->
                MoviesDetail(movie = movie, onBackClick = { navigateBackToListPage() })
            }
        }
    }
}









