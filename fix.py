import re

# MAInACTIVITY
with open("app/src/main/java/com/example/shale_nammapride/MainActivity.kt", "r") as f:
    text = f.read()

import_str = """
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
"""
text = text.replace('import androidx.compose.runtime.saveable.rememberSaveable', 'import androidx.compose.runtime.saveable.rememberSaveable\n' + import_str)

local_prov = """val LocalSnackbar = staticCompositionLocalOf<SnackbarHostState> { error("No Snackbar provided") }

@Composable
private fun AppRoot(viewModel: MainViewModel = hiltViewModel()) {"""
text = text.replace('@Composable\nprivate fun AppRoot(viewModel: MainViewModel = hiltViewModel()) {', local_prov)

snack_val = """    val snackbarHostState = remember { SnackbarHostState() }
    val onLanguageToggle = {"""
text = text.replace('    val onLanguageToggle = {', snack_val)

scaffold_start = """
            var innerPaddingTemp = PaddingValues(0.dp)
            CompositionLocalProvider(LocalSnackbar provides snackbarHostState) {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    content = { innerPadding ->
"""
text = text.replace('            var innerPadding = PaddingValues(0.dp)\n', scaffold_start)

navhost_old = """        NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {"""
navhost_new = """        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400)) }
        ) {"""
text = text.replace(navhost_old, navhost_new)

text = text.replace('        }\n    }\n}', '        }\n                })\n            }\n        }\n    }\n}')

with open("app/src/main/java/com/example/shale_nammapride/MainActivity.kt", "w") as f:
    f.write(text)

# SCREENS
with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "r") as f:
    text = f.read()

imports = """
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import kotlinx.coroutines.launch
import com.example.shale_nammapride.ui.components.ShimmerCardItem
import com.example.shale_nammapride.LocalSnackbar
"""
text = text.replace('import androidx.compose.runtime.saveable.rememberSaveable', 'import androidx.compose.runtime.saveable.rememberSaveable\n' + imports)

# DailyMealScreen
meal_search = "var meals by remember { mutableStateOf(emptyList<DailyMeal>()) }"
meal_rep = "var meals by remember { mutableStateOf(emptyList<DailyMeal>()) }\n    var isLoading by remember { mutableStateOf(true) }"
text = text.replace(meal_search, meal_rep)

text = text.replace('meals = it\n                errorMessage = null', 'meals = it\n                isLoading = false\n                errorMessage = null')

meal_disp_search = "MealDetailCard(meals.firstOrNull(), currentLanguage)"
meal_disp_rep = """AnimatedVisibility(visible = isLoading, enter = expandVertically(), exit = shrinkVertically()) {
                ShimmerCardItem()
            }
            AnimatedVisibility(visible = !isLoading, enter = expandVertically(), exit = shrinkVertically()) {
                MealDetailCard(meals.firstOrNull(), currentLanguage)
            }"""
text = text.replace(meal_disp_search, meal_disp_rep)

snack_rep = """
    val snackbar = LocalSnackbar.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            scope.launch { snackbar.showSnackbar(errorMessage!!) }
        }
    }
"""

text = text.replace('errorMessage?.let {\n                Text(\n                    text = it,\n                    color = androidx.compose.ui.graphics.Color.Red,\n                    modifier = Modifier.padding(top = 8.dp)\n                )\n            }', snack_rep)

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)

