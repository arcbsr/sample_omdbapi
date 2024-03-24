package com.rafiur.assesmentproject.omdb.presentation.ui

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.rafiur.assesmentproject.omdb.domain.models.Movie
import com.rafiur.assesmentproject.omdb.presentation.viewmodel.MovieListViewModel
import com.rafiur.assesmentproject.omdb.presentation.viewmodel.MovieState
import com.rafiur.assesmentproject.utils.SortMenu
import com.rafiur.assesmentproject.utils.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MovieListNorm(
    viewModel: MovieListViewModel,
    viewState: MovieState,
    scrollState: LazyListState,
    isItemReachEndScroll: Boolean
) {
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            LoadingDialog(
                onDismiss = { isLoading = false }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(state = scrollState) {
            items(viewModel.movies.value) { item ->
                MovieListItem(movie = item, 0)
            }
        }

    }
    when (viewState) {
        is MovieState.Loading -> {
            isLoading = true
        }

        is MovieState.Success -> {
            isLoading = false
            Box(modifier = Modifier.fillMaxSize()) {

                LaunchedEffect(key1 = isItemReachEndScroll, block = {
                    if (isItemReachEndScroll)
                        viewModel.fetchMovieList()
                })

            }

        }

        is MovieState.Error -> {
            isLoading = false
        }

    }

}

@Composable
fun TopBar(
    title: String,
    onSortSelected: (SortOption) -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            SortMenu(onSortSelected = onSortSelected)
        }
    )
}

@Composable
fun MovieListItem(movie: Movie, itemIndex: Int) {
    var showDetail by remember { mutableStateOf(false) }
    if (showDetail) {
        MovieDetailsDialog(movie = movie) {
            showDetail = false
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                // Show detail for movie...
                showDetail = true
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.poster),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = itemIndex.toString() + "/" + movie.pageNumber.toString() + "/" + movie.title)
            Text(text = movie.year)
            Text(text = movie.type)
        }
    }
}

@Composable
fun LoadingDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Loading...") },
        buttons = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

            }
        }
    )
}
