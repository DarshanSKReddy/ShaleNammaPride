package com.example.shale_nammapride.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        DrawableAssetOrHint(
            spec = AssetSpec(
                fileNameWithoutExtension = "splash_background",
                folderHint = "res/drawable",
                fallbackEmoji = ""
            ),
            modifier = Modifier.fillMaxSize(),
            showHint = false,
            background = Color.White
        )

        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))
        ) {
            DrawableAssetOrHint(
                spec = AssetSpec(
                    fileNameWithoutExtension = "app_logo",
                    folderHint = "res/drawable",
                    fallbackEmoji = "📚"
                ),
                modifier = Modifier.size(220.dp).padding(24.dp),
                showHint = false
            )
        }
    }

    // Automatically transition after 2 seconds
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds
        onSplashComplete()
    }
}
