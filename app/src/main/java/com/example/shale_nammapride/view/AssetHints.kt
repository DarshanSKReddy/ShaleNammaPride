package com.example.shale_nammapride.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AssetSpec(
    val fileNameWithoutExtension: String,
    val folderHint: String,
    val fallbackEmoji: String
)

@Composable
fun DrawableAssetOrHint(
    spec: AssetSpec,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    showHint: Boolean = true,
    background: Color = Color(0xFFEFF3F8)
) {
    val resourceId = when (spec.fileNameWithoutExtension) {
        "app_logo" -> com.example.shale_nammapride.R.drawable.app_logo
        else -> {
            val context = LocalContext.current
            context.resources.getIdentifier(spec.fileNameWithoutExtension, "drawable", context.packageName)
        }
    }

    if (resourceId != 0) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = spec.fileNameWithoutExtension,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier.background(background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = spec.fallbackEmoji, fontSize = 34.sp)
                if (showHint) {
                    Text(
                        text = "Add ${spec.fileNameWithoutExtension}.png in ${spec.folderHint}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HeroAssetCard(
    spec: AssetSpec,
    modifier: Modifier = Modifier,
    overlayTitle: String? = null,
    overlaySubtitle: String? = null,
    heightDp: androidx.compose.ui.unit.Dp = 180.dp,
    contentScale: ContentScale = ContentScale.Crop,
    showOverlayScrim: Boolean = true
) {
    val shape = MaterialTheme.shapes.large

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp)
            .clip(shape)
    ) {
        DrawableAssetOrHint(
            spec = spec,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            showHint = true,
            background = Color(0xFFF5F7FA)
        )

        if (showOverlayScrim) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x66000000)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            overlayTitle?.let {
                Text(
                    text = it,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            overlaySubtitle?.let {
                Text(
                    text = it,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
