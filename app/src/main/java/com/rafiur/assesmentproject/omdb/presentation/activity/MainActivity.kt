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
import com.rafiur.assesmentproject.omdb.presentation.ui.MovieListUI
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
                MovieListUI()
            }
        }

    }


}