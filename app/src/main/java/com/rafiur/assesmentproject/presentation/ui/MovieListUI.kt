package com.rafiur.assesmentproject.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rafiur.assesmentproject.domain.models.Movie
import com.rafiur.assesmentproject.presentation.viewmodel.MovieListViewModel
import com.rafiur.assesmentproject.presentation.viewmodel.MovieState
import com.rafiur.assesmentproject.utils.SortMenu
import com.rafiur.assesmentproject.utils.SortOption

@Composable
fun MovieListNorm(
    viewModel: MovieListViewModel,
    viewState: MovieState,
    scrollState: LazyListState,
    isItemReachEndScroll: Boolean
) {

    LazyColumn(state = scrollState) {
        items(viewModel.movies.value) { item ->
            MovieListItem(movie = item, 0)
        }
    }

    when (viewState) {
        is MovieState.Loading -> {
        }

        is MovieState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {

                LaunchedEffect(key1 = isItemReachEndScroll, block = {
                    if (isItemReachEndScroll)
                        viewModel.fetchMovieList()
                })

//                LazyColumn(state = scrollState) {
//                    itemsIndexed(viewState.movies) { index, movie ->
//                        MovieListItem(movie = movie, index)
//                    }
//                }
            }

        }

        is MovieState.Error -> {
            // Show error message
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(movie.poster),
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