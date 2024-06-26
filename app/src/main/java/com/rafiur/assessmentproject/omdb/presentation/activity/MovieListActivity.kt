package com.rafiur.assessmentproject.omdb.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.rafiur.assessmentproject.omdb.presentation.ui.MovieListUI
import com.rafiur.assessmentproject.omdb.presentation.viewmodel.MovieListViewModel
import com.rafiur.assessmentproject.ui.theme.AssesmentProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListActivity : ComponentActivity() {

    private val movieViewModel: MovieListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssesmentProjectTheme {
                MovieListUI(movieViewModel)
            }
        }

    }


}