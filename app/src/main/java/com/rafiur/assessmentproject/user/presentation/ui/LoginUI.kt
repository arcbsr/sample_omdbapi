package com.rafiur.assessmentproject.user.presentation.ui

import android.content.Intent
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.rafiur.assessmentproject.R
import com.rafiur.assessmentproject.omdb.presentation.activity.MovieListActivity
import com.rafiur.assessmentproject.user.presentation.viewmodels.AuthState
import com.rafiur.assessmentproject.user.presentation.viewmodels.AuthViewModel

@Composable
fun LoginUI(authViewModel: AuthViewModel) {
    authViewModel.checkCurrentUser()
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.welcome),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center

                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = AppBarDefaults.TopAppBarElevation
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LoginPanel(authViewModel = authViewModel)
            }

        }
    }
}

@Composable
fun LoginPanel(authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("re@gmail.com") }
    var errorMsg by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("123456") }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val context = LocalContext.current as FragmentActivity
    val biometricManager = BiometricManager.from(context)
    val canAuthenticateWithBiometrics =
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> {
                Log.e("TAG", "Device does not support strong biometric authentication")
                false
            }
        }
    Surface {
        if (isLoading) {
            loadingDialog()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(32.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.email)) },
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(id = R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        if (!isLoading) {
                            if (isEmailValid(email) && isPasswordValid(password)) {
                                isLoading = true
                                isError = false
                                authViewModel.signIn(email, password)
                            } else {
                                isError = true
                                errorMsg = context.getString(R.string.invalid_id_pass)
                            }
                        }
                    }, modifier = Modifier.width(150.dp)) {
                        Text(text = stringResource(id = R.string.login))
                    }
                    Button(
                        onClick = {
                            if (!isLoading) {
                                if (isEmailValid(email) && isPasswordValid(password)) {
                                    isLoading = true
                                    isError = false
                                    authViewModel.signUp(email, password)
                                } else {
                                    isError = true
                                    errorMsg = context.getString(R.string.invalid_id_pass)
                                }

                            }
                        }, modifier = Modifier
                            .width(150.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text(
                            text = stringResource(id = R.string.create_new),
                            style = TextStyle(color = Color.White)
                        )
                    }
                }

                if (isError) {
                    Text(
                        text = errorMsg,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    LaunchedEffect(key1 = authViewModel.authState) {
        authViewModel.authState.collect { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    isLoading = false
                    isError = false
                    if (canAuthenticateWithBiometrics) {
                        authenticateWithBiometric(context)
                    } else {
                        isLoading = false
                        isError = true
                        errorMsg = context.getString(R.string.biometric_error)
                        gotoMovieListScreen(context)
                    }
                }

                is AuthState.Unauthenticated -> {
                    isLoading = false
                    isError = false
                }

                is AuthState.Loading -> {
                    errorMsg = ""
                    isLoading = true
                    isError = false
                }

                is AuthState.AuthInError -> {
                    isLoading = false
                    isError = true
                    errorMsg = authState.msg
                }
            }
        }
    }
}

@Composable
fun BiometricButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = text)
    }
}


private fun gotoMovieListScreen(context: FragmentActivity) {
    val intent = Intent(context, MovieListActivity::class.java)
    context.startActivity(intent)
    context.finish()
}

fun authenticateWithBiometric(context: FragmentActivity) {
    val executor = context.mainExecutor
    val biometricPrompt = BiometricPrompt(
        context,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                gotoMovieListScreen(context)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {

            }

            override fun onAuthenticationFailed() {

            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.biometric_auth_title))
        .setDescription(context.getString(R.string.biometric_auth_description))
        .setNegativeButtonText(context.getString(R.string.btn_cancel))
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    biometricPrompt.authenticate(promptInfo)
}

@Composable
fun loadingDialog() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}

// Function to validate email format
fun isEmailValid(email: String): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(email)
}

// Function to validate password format
fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
}