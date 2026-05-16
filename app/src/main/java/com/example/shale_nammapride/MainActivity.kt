package com.example.shale_nammapride

import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shale_nammapride.view.*
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { AppRoot() }
        }
    }
}

@Composable
private fun AppRoot() {
    val auth = remember { FirebaseAuth.getInstance() }
    var showSplash by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    var currentLanguage by rememberSaveable { mutableStateOf("en") }

    val onLanguageToggle = {
        val newLang = if (currentLanguage == "en") "kn" else "en"
        currentLanguage = newLang
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLang))
        Locale.setDefault(Locale.Builder().setLanguage(newLang).build())
    }

    if (showSplash) {
        SplashScreen(onSplashComplete = { showSplash = false })
        return
    }

    if (isLoggedIn) {
        AppNavigationWithHome(
            currentLanguage = currentLanguage,
            onLanguageToggle = onLanguageToggle,
            onLogout = {
                auth.signOut()
                isLoggedIn = false
                currentLanguage = "en"
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
            }
        )
    } else {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    }
}

@Composable
fun AppNavigationWithHome(
    currentLanguage: String,
    onLanguageToggle: () -> Unit,
    onLogout: () -> Unit
) {
    val isCompactScreen = LocalConfiguration.current.screenWidthDp < 390
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = stringResource(id = R.string.home)) },
                    label = {
                        Text(
                            text = localizedText(currentLanguage, "Home", "ಮುಖ್ಯಪುಟ"),
                            fontSize = if (isCompactScreen) 10.sp else 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = currentRoute == "home",
                    onClick = { navigate("home") },
                    alwaysShowLabel = !isCompactScreen
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = stringResource(id = R.string.daily_meal)) },
                    label = {
                        Text(
                            text = localizedText(currentLanguage, "Daily Meal", "ದಿನಸಿ ಊಟ"),
                            fontSize = if (isCompactScreen) 10.sp else 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = currentRoute == "meal",
                    onClick = { navigate("meal") },
                    alwaysShowLabel = !isCompactScreen
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Place, contentDescription = stringResource(id = R.string.facilities)) },
                    label = {
                        Text(
                            text = localizedText(currentLanguage, "Facilities", "ಸೌಕರ್ಯಗಳು"),
                            fontSize = if (isCompactScreen) 10.sp else 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = currentRoute == "facility",
                    onClick = { navigate("facility") },
                    alwaysShowLabel = !isCompactScreen
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = stringResource(id = R.string.student_stars)) },
                    label = {
                        Text(
                            text = localizedText(currentLanguage, "Student Stars", "ವಿದ್ಯಾರ್ಥಿ ನಕ್ಷತ್ರಗಳು"),
                            fontSize = if (isCompactScreen) 10.sp else 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = currentRoute == "stars",
                    onClick = { navigate("stars") },
                    alwaysShowLabel = !isCompactScreen
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = stringResource(id = R.string.feedback)) },
                    label = {
                        Text(
                            text = localizedText(currentLanguage, "Feedback", "ಮಾಹಿತಿ ಸಂಗ್ರಹ"),
                            fontSize = if (isCompactScreen) 10.sp else 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = currentRoute == "feedback",
                    onClick = { navigate("feedback") },
                    alwaysShowLabel = !isCompactScreen
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
            composable("home") {
                HomeScreen(
                    onNavigateTo = { route -> navigate(route) },
                    onLanguageToggle = onLanguageToggle,
                    currentLanguage = currentLanguage,
                    onLogout = onLogout
                )
            }
            composable("admin") { AdminUploadScreen(onBackPressed = { navController.popBackStack() }) }
            composable("meal") { DailyMealScreen(currentLanguage = currentLanguage, onBackPressed = { navController.popBackStack() }) }
            composable("facility") { FacilityScreen(currentLanguage = currentLanguage, onBackPressed = { navController.popBackStack() }) }
            composable("stars") { StarsScreen(currentLanguage = currentLanguage, onBackPressed = { navController.popBackStack() }) }
            composable("feedback") { FeedbackScreen(currentLanguage = currentLanguage, onBackPressed = { navController.popBackStack() }) }
        }
    }
}