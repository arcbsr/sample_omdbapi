package com.rafiur.assesmentproject.user.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.rafiur.assesmentproject.omdb.presentation.activity.MainActivity
import com.rafiur.assesmentproject.user.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import com.rafiur.assesmentproject.user.presentation.viewmodels.AuthState

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel.checkCurrentUser()
        setContent {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "WELCOME",
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
                        EmailPasswordForm(authViewModel)

                    }

                }
            }
        }
    }
}

@Composable
fun EmailPasswordForm(authViewModel: AuthViewModel) {
//            (onSubmit: (String, String, Boolean,authViewModel: AuthViewModel) -> Unit) {
    var email by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                Text(text = "Login", style = MaterialTheme.typography.h4)
                Spacer(modifier = Modifier.height(32.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
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
                                errorMsg = "Invalid email or password"
                            }
                        }
                    }, modifier = Modifier.width(150.dp)) {
                        Text(text = "Login")
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
                                    errorMsg = "Invalid email or password"
                                }

                            }
                        }, modifier = Modifier
                            .width(150.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text(
                            text = "Create New",
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
                if (canAuthenticateWithBiometrics) {
                    BiometricButton(
                        onClick = {
                            authenticateWithBiometric(context)
                        },
                        text = "Authenticate with Biometric",
                    )
                } else {
                    Text(text = "Biometric authentication is not available on this device.")
                }

            }
        }
    }
    LaunchedEffect(key1 = authViewModel.authState) {
        authViewModel.authState.collect { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    isLoading = true
                    isError = false
                    context.finish()

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

fun authenticateWithBiometric(context: FragmentActivity) {
    val executor = context.mainExecutor
    val biometricPrompt = BiometricPrompt(
        context,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                //TODO handle authentication success, proceed to HomeScreen
                Log.d("TAG", "Authentication successful!!!")
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Log.e("TAG", "onAuthenticationError")
                //TODO Handle authentication errors.
            }

            override fun onAuthenticationFailed() {
                Log.e("TAG", "onAuthenticationFailed")
                //TODO Handle authentication failures.
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setDescription("Place your finger the sensor or look at the front camera to authenticate.")
        .setNegativeButtonText("Cancel")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    biometricPrompt.authenticate(promptInfo)
}