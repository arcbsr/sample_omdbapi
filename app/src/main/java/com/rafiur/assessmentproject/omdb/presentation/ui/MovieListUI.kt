package com.rafiur.assessmentproject.omdb.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rafiur.assessmentproject.omdb.domain.models.Movie
import com.rafiur.assessmentproject.omdb.presentation.viewmodel.MovieListViewModel
import com.rafiur.assessmentproject.omdb.presentation.viewmodel.MovieState


@Composable
fun MovieListUI(movieViewModel: MovieListViewModel) {
    movieViewModel.fetchMovieList()
    Column {
        TopAppBar(
            title = {

                androidx.compose.material.Text(
                    text = "MOVIES",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center

                )

            },
        )

        MovieListNorm(movieViewModel)


    }
}

@Composable
fun MovieListNorm(
    viewModel: MovieListViewModel,
) {
    val viewState by viewModel.mState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isEndOfSearch by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val isItemReachEndScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == scrollState.layoutInfo.totalItemsCount - 1
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            LoadingDialog(onDismiss = { isLoading = false })
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(state = scrollState) {
            items(viewModel.movies.value) { item ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),

                    ) {
                    MovieListItem(movie = item)
                }
            }
        }

    }

    when (viewState) {
        is MovieState.Loading -> {
            isLoading = true
        }

        is MovieState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {

                LaunchedEffect(key1 = isItemReachEndScroll, block = {
                    if (!isLoading && !isEndOfSearch)
                        if (isItemReachEndScroll) viewModel.fetchMovieList()
                })

            }
            isLoading = false
        }

        is MovieState.Error -> {
            isLoading = false
        }

        MovieState.EndOfSearch -> {
            isLoading = false
            isEndOfSearch = true
        }
    }

}
@Composable
fun MovieListItem(movie: Movie) {
    var showDetail by remember { mutableStateOf(false) }
    if (showDetail) {
        MovieDetailsDialog(movie = movie) {
            showDetail = false
        }
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {
            // Show detail for movie...
            showDetail = true
        }) {
        Image(
            painter = rememberAsyncImagePainter(movie.poster),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            contentScale = ContentScale.Crop,

            )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = movie.title)
            Text(text = movie.year)
            Text(text = movie.type)
        }
    }
}

@Composable
fun LoadingDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text(text = "Loading...") }, buttons = {
        Row(
            modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center
        ) {

        }
    })
}