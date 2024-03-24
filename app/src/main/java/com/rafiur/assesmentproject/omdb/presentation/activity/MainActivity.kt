package com.rafiur.assesmentproject.omdb.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafiur.assesmentproject.omdb.presentation.ui.MovieListNorm
import com.rafiur.assesmentproject.omdb.presentation.ui.TopBar
import com.rafiur.assesmentproject.omdb.presentation.viewmodel.MovieListViewModel
import com.rafiur.assesmentproject.ui.theme.AssesmentProjectTheme
import com.rafiur.assesmentproject.utils.SortOption
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssesmentProjectTheme {
                val viewModel: MovieListViewModel = viewModel()
                viewModel.fetchMovieList()
                MainActivityContent(viewModel)
            }
        }
    }

    @Composable
    fun MainActivityContent(viewModel: MovieListViewModel) {
        val viewState by viewModel.mState.collectAsState()
        val scrollState = rememberLazyListState()
        val isItemReachEndScroll by remember {
            derivedStateOf() {
                scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
                        scrollState.layoutInfo.totalItemsCount - 1
            }
        }
        // Display the TopBar and MovieList composables in a Column
        Column {
            TopAppBar(
                title = {

                        Text(
                            text = "MOVIES",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center

                        )

                },
//                onSortSelected = { sortOption ->
//                    when (sortOption) {
//                        SortOption.NAME_ASCENDING -> {
//                            viewModel.sortByNameAscending()
//                        }
//
//                        SortOption.NAME_DESCENDING -> {
//                            viewModel.sortByNameDescending()
//                        }
//
//                        SortOption.DATE_ASCENDING -> {
//                            viewModel.sortByYearAscending()
//                        }
//
//                        SortOption.DATE_DESCENDING -> {
//                            viewModel.sortByYearDescending()
//                        }
//                    }
//
//                }
            )
            MovieListNorm(viewModel, viewState, scrollState, isItemReachEndScroll)

        }
    }
}

//    private val BASE_URL = "https://www.omdbapi.com/"
//    private lateinit var apiService: ApiService
// Initialize Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        // Create an instance of ApiService
//        apiService = retrofit.create(ApiService::class.java)
//        GlobalScope.launch(Dispatchers.Main) {
//            // Call the suspend function from ApiService
//            try {
//                val searchResponse = apiService.searchMovies("love","666da4ed")
//                // Process the response data here
//                Log.w("getdata", searchResponse.search.size.toString())
//            } catch (e: Exception) {
//                // Handle errors
////                showError("Failed to fetch data: ${e.message}")
//                Log.w("getdata", e.message.toString())
//            }
//        }