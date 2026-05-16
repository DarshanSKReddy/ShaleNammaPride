package com.example.shale_nammapride.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shale_nammapride.R


@Composable
fun HomeScreen(
    onNavigateTo: (String) -> Unit,
    onLanguageToggle: () -> Unit,
    currentLanguage: String,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isCompactScreen = LocalConfiguration.current.screenWidthDp < 390
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.light_blue_bg))
                .padding(
                    horizontal = if (isCompactScreen) 14.dp else 20.dp,
                    vertical = if (isCompactScreen) 12.dp else 18.dp
                )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (isCompactScreen) 10.dp else 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DrawableAssetOrHint(
                        spec = AssetSpec(
                            fileNameWithoutExtension = "app_logo",
                            folderHint = "res/drawable",
                            fallbackEmoji = "📚"
                        ),
                        modifier = Modifier.size(if (isCompactScreen) 100.dp else 120.dp),
                        contentScale = ContentScale.Fit,
                        showHint = false
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        LanguageToggleButton(
                            currentLanguage = currentLanguage,
                            onToggle = onLanguageToggle
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(
                                onClick = { onNavigateTo("admin") },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = localizedText(currentLanguage, "Admin Upload", "ಆಡಳಿತ ಅಪ್‌ಲೋಡ್"),
                                    fontSize = if (isCompactScreen) 11.sp else 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            TextButton(
                                onClick = onLogout,
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = localizedText(currentLanguage, "Logout", "ಲಾಗ್ ಔಟ್"),
                                    fontSize = if (isCompactScreen) 11.sp else 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                HeroAssetCard(
                    spec = AssetSpec(
                        fileNameWithoutExtension = "home_banner",
                        folderHint = "res/drawable",
                        fallbackEmoji = "🏫☀️"
                    ),
                    overlayTitle = localizedText(currentLanguage, "Welcome!", "ಸ್ವಾಗತ!"),
                    overlaySubtitle = localizedText(currentLanguage, "Together, let's build a better tomorrow", "ಒಟ್ಟಾಗಿ, ಒಂದು ಉತ್ತಮ ಭವಿಷ್ಯತ್ ತರೋಲಿ ನಿರ್ಮಾಣ ಮಾಡೋಣ"),
                    heightDp = if (isCompactScreen) 145.dp else 160.dp,
                    modifier = Modifier.padding(top = if (isCompactScreen) 8.dp else 12.dp)
                )
            }
        }

        Text(
            text = localizedText(currentLanguage, "Quick Access", "ತ್ವರಿತ ಪ್ರವೇಶ"),
            fontSize = if (isCompactScreen) 14.sp else 15.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.primary_dark_blue),
            modifier = Modifier.padding(
                start = if (isCompactScreen) 14.dp else 16.dp,
                top = 4.dp,
                bottom = if (isCompactScreen) 8.dp else 12.dp
            )
        )

        Column(
            modifier = Modifier.padding(
                horizontal = if (isCompactScreen) 14.dp else 16.dp,
                vertical = if (isCompactScreen) 6.dp else 8.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (isCompactScreen) 10.dp else 12.dp),
                horizontalArrangement = Arrangement.spacedBy(if (isCompactScreen) 10.dp else 12.dp)
            ) {
                FeatureCard(
                    emoji = "🍲",
                    assetSpec = AssetSpec("card_meal", "res/drawable", "🍲"),
                    title = localizedText(currentLanguage, "Daily Meal", "ದಿನಸಿ ಊಟ"),
                    description = localizedText(currentLanguage, "Menu and meal photo", "ಮೆನು ಮತ್ತು ಊಟದ ಫೋಟೋ"),
                    backgroundColor = Color(0xFFF6D57D), // The 'FF' at the start represents 100% alpha (opacity)
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo("meal") }
                )
                
                FeatureCard(
                    emoji = "🏛️",
                    assetSpec = AssetSpec("card_facilities", "res/drawable", "🏛️"),
                    title = localizedText(currentLanguage, "Facilities", "ಸೌಕರ್ಯಗಳು"),
                    description = localizedText(currentLanguage, "Gallery of school spaces", "ಶಾಲೆಯ ಸ್ಥಳಗಳ ಗ್ಯಾಲರಿ"),
                    backgroundColor = Color(0xFF80C7F9),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo("facility") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(if (isCompactScreen) 10.dp else 12.dp)
            ) {
                FeatureCard(
                    emoji = "🏆",
                    assetSpec = AssetSpec("card_stars", "res/drawable", "🏆"),
                    title = localizedText(currentLanguage, "Student Stars", "ವಿದ್ಯಾರ್ಥಿ ನಕ್ಷತ್ರಗಳು"),
                    description = localizedText(currentLanguage, "Celebrate student wins", "ವಿದ್ಯಾರ್ಥಿ ಸಾಧನೆಗಳನ್ನು ಆಚರಿಸಿ"),
                    backgroundColor = Color(0xFFD4C2F7),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo("stars") }
                )
                
                FeatureCard(
                    emoji = "💬",
                    assetSpec = AssetSpec("card_feedback", "res/drawable", "💬"),
                    title = localizedText(currentLanguage, "Feedback", "ಮಾಹಿತಿ ಸಂಗ್ರಹ"),
                    description = localizedText(currentLanguage, "Share a suggestion", "ಸಲಹೆ ಹಂಚಿಕೊಳ್ಳಿ"),
                    backgroundColor = Color(0xFFbadbFa),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo("feedback") }
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (isCompactScreen) 14.dp else 16.dp,
                    vertical = if (isCompactScreen) 10.dp else 14.dp
                ),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.light_green_bg)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isCompactScreen) 14.dp else 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🌱", fontSize = if (isCompactScreen) 20.sp else 22.sp, modifier = Modifier.padding(end = 12.dp))
                Text(
                    text = localizedText(currentLanguage, "Every update builds trust, every child builds the future.", "ಪ್ರತಿಯೊಂದು ನವೀಕರಣ ನಂಬಿಕೆ ಬಿಟ್ಟು, ಪ್ರತಿಯೊಂದು ಮಗು ಭವಿಷ್ಯತ್ತನ್ನು ನಿರ್ಮಿಸುತ್ತದೆ."),
                    fontSize = if (isCompactScreen) 10.sp else 11.sp,
                    color = colorResource(id = R.color.primary_dark_blue),
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeatureCard(
    emoji: String,
    assetSpec: AssetSpec,
    title: String,
    description: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DrawableAssetOrHint(
                spec = assetSpec,
                modifier = Modifier
                    .size(78.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Fit,
                showHint = false
            )
            
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primary_dark_blue),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = description,
                fontSize = 9.sp,
                color = colorResource(id = R.color.dark_text),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Navigate",
                tint = colorResource(id = R.color.primary_purple),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LanguageToggleButton(
    currentLanguage: String,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(36.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.english),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentLanguage == "en") Color.White else colorResource(id = R.color.primary_dark_blue),
                modifier = Modifier
                    .background(
                        color = if (currentLanguage == "en") colorResource(id = R.color.primary_purple) else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            
            Text(
                text = stringResource(id = R.string.kannada),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentLanguage == "kn") Color.White else colorResource(id = R.color.primary_dark_blue),
                modifier = Modifier
                    .background(
                        color = if (currentLanguage == "kn") colorResource(id = R.color.primary_purple) else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}
