package com.example.shale_nammapride.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.colorResource
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.shale_nammapride.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var isSignUpMode by rememberSaveable { mutableStateOf(false) }
    val auth = remember { FirebaseAuth.getInstance() }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var loading by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
    ) {
        AuthTopBanner(isSignUpMode = isSignUpMode)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = colorResource(id = R.color.light_gray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.home_title),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primary_dark_blue)
                )

                Text(
                    text = if (isSignUpMode) {
                        stringResource(id = R.string.create_new_account)
                    } else {
                        stringResource(id = R.string.admin_login)
                    },
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.dark_text),
                    modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = if (isSignUpMode) {
                                stringResource(id = R.string.sign_up_to_continue)
                            } else {
                                stringResource(id = R.string.sign_in_to_continue)
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.dark_text),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(id = R.string.email)) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(id = R.string.password)) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (isSignUpMode) {
                            Spacer(modifier = Modifier.height(14.dp))
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(stringResource(id = R.string.confirm_password)) },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        message?.let {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }

                        val passwordsMustMatchMessage = stringResource(id = R.string.passwords_must_match)
                        val passwordMinLengthMessage = stringResource(id = R.string.password_min_length)

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                message = null
                                if (isSignUpMode) {
                                    when {
                                        confirmPassword != password -> {
                                            message = passwordsMustMatchMessage
                                        }

                                        password.length < 6 -> {
                                            message = passwordMinLengthMessage
                                        }

                                        else -> {
                                            loading = true
                                            auth.createUserWithEmailAndPassword(email.trim(), password)
                                                .addOnCompleteListener { task ->
                                                    loading = false
                                                    if (task.isSuccessful) {
                                                        onLoginSuccess()
                                                    } else {
                                                        message = task.exception?.localizedMessage
                                                            ?: "Sign up failed"
                                                    }
                                                }
                                        }
                                    }
                                } else {
                                    loading = true
                                    auth.signInWithEmailAndPassword(email.trim(), password)
                                        .addOnCompleteListener { task ->
                                            loading = false
                                            if (task.isSuccessful) {
                                                onLoginSuccess()
                                            } else {
                                                message = task.exception?.localizedMessage ?: "Login failed"
                                            }
                                        }
                                }
                            },
                            enabled = !loading && email.isNotBlank() && password.isNotBlank() && (!isSignUpMode || confirmPassword.isNotBlank()),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.primary_dark_blue)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (isSignUpMode) {
                                    stringResource(id = R.string.create_account)
                                } else {
                                    stringResource(id = R.string.login)
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        TextButton(
                            onClick = {
                                isSignUpMode = !isSignUpMode
                                message = null
                                password = ""
                                confirmPassword = ""
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = if (isSignUpMode) {
                                    stringResource(id = R.string.already_have_account)
                                } else {
                                    stringResource(id = R.string.no_account_sign_up)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stringResource(id = R.string.or_continue_with),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.dark_text),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GoogleSignInButton(
                            loading = loading,
                            onSignInSuccess = onLoginSuccess,
                            onError = { errorMsg -> message = errorMsg },
                            onLoadingChange = { loading = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AuthTopBanner(isSignUpMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(colorResource(id = R.color.light_blue_bg))
    ) {
        DrawableAssetOrHint(
            spec = AssetSpec(
                fileNameWithoutExtension = if (isSignUpMode) "signup_banner" else "login_banner",
                folderHint = "res/drawable",
                fallbackEmoji = "🏫"
            ),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            showHint = false
        )
    }
}

@Composable
fun GoogleSignInButton(
    loading: Boolean,
    onSignInSuccess: () -> Unit,
    onError: (String) -> Unit,
    onLoadingChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (data == null) {
            onLoadingChange(false)
            onError("Google sign-in cancelled")
            return@rememberLauncherForActivityResult
        }

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken.isNullOrBlank()) {
                onLoadingChange(false)
                onError("Unable to fetch Google ID token. Check SHA-1 and google-services.json")
                return@rememberLauncherForActivityResult
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    onLoadingChange(false)
                    if (authTask.isSuccessful) {
                        onSignInSuccess()
                    } else {
                        onError(authTask.exception?.localizedMessage ?: "Google sign-in failed")
                    }
                }
        } catch (e: ApiException) {
            onLoadingChange(false)
            onError(e.localizedMessage ?: "Google sign-in error")
        }
    }

    Button(
        onClick = {
            onLoadingChange(true)
            signInWithGoogle(
                context = context,
                launcher = launcher,
                onError = onError,
                onLoadingChange = onLoadingChange
            )
        },
        enabled = !loading,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sign in with Google",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.primary_dark_blue)
            )
            Text(
                text = "Fast and secure",
                fontSize = 10.sp,
                color = colorResource(id = R.color.dark_text),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getDefaultWebClientId(context: Context): String {
    val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
    return if (resId != 0) context.getString(resId) else ""
}

private fun isValidWebClientId(clientId: String): Boolean {
    return clientId.isNotBlank() && clientId.contains("apps.googleusercontent.com")
}

private fun signInWithGoogle(
    context: Context,
    launcher: ActivityResultLauncher<Intent>,
    onError: (String) -> Unit,
    onLoadingChange: (Boolean) -> Unit
) {
    val webClientId = getDefaultWebClientId(context)
    if (!isValidWebClientId(webClientId)) {
        onLoadingChange(false)
        onError("Google Sign-In not ready. Add SHA-1 in Firebase and download updated google-services.json")
        return
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(webClientId)
        .requestEmail()
        .build()

    val signInIntent = GoogleSignIn.getClient(context, gso).signInIntent
    launcher.launch(signInIntent)
}