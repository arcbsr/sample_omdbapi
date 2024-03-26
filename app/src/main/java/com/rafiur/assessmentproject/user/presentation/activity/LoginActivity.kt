package com.rafiur.assessmentproject.user.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rafiur.assessmentproject.ui.theme.AssesmentProjectTheme
import com.rafiur.assessmentproject.user.presentation.ui.LoginUI
import com.rafiur.assessmentproject.user.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AssesmentProjectTheme {
                LoginUI(authViewModel = authViewModel)
            }
        }
    }
}
