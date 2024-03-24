package com.rafiur.assesmentproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafiur.assesmentproject.domain.ResponseWrapper
import com.rafiur.assesmentproject.domain.models.Movie
import com.rafiur.assesmentproject.domain.remote.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class MovieListViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    // Coroutine scope for debouncing
    private val debounceScope = CoroutineScope(Dispatchers.Default)

    // Debounce function to limit the frequency of API calls
    @OptIn(ExperimentalTime::class)
    fun fetchMovieListDebounced(
        delayMillis: Long = 1000, // Adjust the delay as needed
        action: () -> Unit
    ) {
        debounceScope.launch {
            delay(delayMillis)
            action()
        }
    }

    private val _state = MutableStateFlow<MovieState>(MovieState.Loading)
    val mState: StateFlow<MovieState> = _state

    private val _movies: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    private val _pageNumber: MutableStateFlow<Int> = MutableStateFlow(0) // Initial page number
    val pageNumber: StateFlow<Int> get() = _pageNumber

    private val _blockLoading: MutableStateFlow<Boolean> =
        MutableStateFlow(false) // Initial page number
    val blockLoading: StateFlow<Boolean> get() = _blockLoading

    fun fetchMovieList() {
        if (_blockLoading.value) {
            return
        }
        fetchMovieListDebounced {
            _blockLoading.value = false
        }
        _state.value = MovieState.Loading
        _blockLoading.value = true
        viewModelScope.launch {
            _pageNumber.value++
            //_state.value = CreateViewState.Init
            useCase.getMovies("love", page = _pageNumber.value).onStart {
//                _state.value = CreateViewState.IsLoading(true)
            }.catch {


                _state.value = MovieState.Error("Unknown")
            }.collect { it ->
//                _state.value = MovieState.Loading
                when (it) {
                    is ResponseWrapper.GenericError -> {
                        it.error?.let { msg ->
                            Log.w("ErrorResponse", msg)
                            _state.value =
                                MovieState.Error("${it.code} : $msg")
                        }
//                        _blockLoading.value = false
                    }

                    ResponseWrapper.NetworkError -> {
                        Log.w("ErrorResponse", "Network Error")
                        _state.value = MovieState.Error("Network Error")
//                        _blockLoading.value = false
                    }

                    is ResponseWrapper.Success -> {
                        if(it.value?.search == null || it.value.search.isEmpty()){
                            _state.value = MovieState.Success(_movies.value)
                        }else {
                            Log.w("GetData>>", "Success >>" + it.value.search.size.toString())
                            for (movie in it.value.search) {
                                movie.pageNumber = _pageNumber.value
                            }
                            addNewMovies(it.value.search)
//                        _blockLoading.value = false
                        }
                    }
                }
            }
        }
    }

    private fun addNewMovies(newMovies: List<Movie>) {
        val currentMovies = _movies.value.toMutableList() // Convert current data to a mutable list
        currentMovies.addAll(newMovies) // Add new movies to the existing list
        _movies.value = currentMovies.toList() // Update the state flow with the updated list
        _state.value =
//            MovieState.Success(_movies.value.sortedByDescending { it.year })
            MovieState.Success(_movies.value)
        Log.w("GetData>>", "total >>" + _movies.value.size.toString())
    }

//    fun <T : Any> List<T>.toPagingData(): Flow<PagingData<T>> {
//        val pagingSourceFactory = { ListPagingSource(this) }
//        return Pager(
//            config = PagingConfig(pageSize = this.size),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow
//    }

    fun sortByNameAscending() {
        val currentState = _state.value
        if (currentState is MovieState.Success) {
            val currentData = currentState.movies.toMutableList()
            val sortedMovies = currentData.sortedBy { it.title }
            _state.value = MovieState.Success(sortedMovies)
        }
    }

    private fun addNewData(search: List<Movie>) {
        val currentState = _state.value
        if (currentState is MovieState.Success) {
            val currentData = currentState.movies.toMutableList()
            currentData.addAll(search.sortedByDescending { it.year })
            Log.w("Data Added", currentState.movies.size.toString())
        }


    }

    fun sortByNameDescending() {
        val currentState = _state.value
        if (currentState is MovieState.Success) {
            val currentData = currentState.movies.toMutableList()
            val sortedMovies = currentData.sortedByDescending { it.title }
            _state.value = MovieState.Success(sortedMovies)
        }
    }

    fun sortByYearAscending() {
        val currentState = _state.value
        if (currentState is MovieState.Success) {
            val currentData = currentState.movies.toMutableList()
            val sortedMovies = currentData.sortedBy { it.year }
            _state.value = MovieState.Success(sortedMovies)
        }
    }

    fun sortByYearDescending() {
        val currentState = _state.value
        if (currentState is MovieState.Success) {
            val currentData = currentState.movies.toMutableList()
            val sortedMovies = currentData.sortedByDescending { it.year }
            _state.value = MovieState.Success(sortedMovies)
        }
    }
}

sealed class MovieState {
    object Loading : MovieState()
    data class Success(val movies: List<Movie>) : MovieState()
    data class Error(val message: String) : MovieState()
}
