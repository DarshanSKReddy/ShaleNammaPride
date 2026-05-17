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

text = text.replace('var innerPadding = PaddingValues(0.dp)', 'var innerPadding0 = PaddingValues(0.dp)\n        CompositionLocalProvider(LocalSnackbar provides snackbarHostState) {\n            Scaffold(\n                snackbarHost = { SnackbarHost(snackbarHostState) },\n                content = { innerPadding ->')

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

if text.endswith("        }\n    }\n}\n"):
    text = text[:-16] + "        }\n            })\n        }\n    }\n}\n"

with open("app/src/main/java/com/example/shale_nammapride/MainActivity.kt", "w") as f:
    f.write(text)
