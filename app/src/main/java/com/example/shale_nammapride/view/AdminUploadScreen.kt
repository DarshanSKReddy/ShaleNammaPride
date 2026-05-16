package com.example.shale_nammapride.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.launch
import com.example.shale_nammapride.R
import com.example.shale_nammapride.data.GeminiTranslator
import com.example.shale_nammapride.data.FirebaseContentRepository
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AdminUploadScreen(onBackPressed: () -> Unit = {}) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_blue_bg))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Column {
                    Text(
                        text = stringResource(id = R.string.admin_upload),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.admin_panel_subtitle),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text(stringResource(id = R.string.meal_tab)) })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text(stringResource(id = R.string.facility_tab)) })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text(stringResource(id = R.string.star_tab)) })
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            HeroAssetCard(
                spec = AssetSpec(
                    fileNameWithoutExtension = "admin_banner",
                    folderHint = "res/drawable",
                    fallbackEmoji = "🛠️"
                ),
                overlayTitle = stringResource(id = R.string.admin_upload),
                overlaySubtitle = stringResource(id = R.string.admin_panel_subtitle),
                heightDp = 220.dp,
                contentScale = ContentScale.Fit,
                showOverlayScrim = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.upload_hint_storage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            when (selectedTab) {
                0 -> MealUploadForm()
                1 -> FacilityUploadForm()
                2 -> StudentStarUploadForm()
            }
        }
    }
}

@Composable
private fun MealUploadForm() {
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    var menuEn by rememberSaveable { mutableStateOf("") }
    var menuKn by rememberSaveable { mutableStateOf("") }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var posting by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pickedImageUri = uri
        statusMessage = if (uri != null) null else statusMessage
    }

    val postedBy = remember { FirebaseAuth.getInstance().currentUser?.email ?: "admin" }

    AdminCard {
        Button(
            onClick = { imagePicker.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_dark_blue)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(id = R.string.select_image))
        }
        pickedImageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.image_selected), style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text(stringResource(id = R.string.image_url)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(id = R.string.image_or_url_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = menuEn,
            onValueChange = { menuEn = it },
            label = { Text(stringResource(id = R.string.menu_english)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = menuKn,
            onValueChange = { menuKn = it },
            label = { Text(stringResource(id = R.string.menu_kannada)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    posting = true
                    statusMessage = null
                    val finalMenuKn = resolveKannadaText(menuEn, menuKn)
                    val finalizeSave: (String) -> Unit = { finalImageUrl ->
                        FirebaseContentRepository.saveDailyMeal(
                            imageUrl = finalImageUrl,
                            menuEnglish = menuEn.trim(),
                            menuKannada = finalMenuKn,
                            postedBy = postedBy
                        ) { success, message ->
                            posting = false
                            statusMessage = message
                            if (success) {
                                menuEn = ""
                                menuKn = ""
                                imageUrl = ""
                                pickedImageUri = null
                            }
                        }
                    }
                    val trimmedImageUrl = imageUrl.trim()
                    if (trimmedImageUrl.isNotBlank()) {
                        finalizeSave(trimmedImageUrl)
                    } else if (pickedImageUri != null) {
                        FirebaseContentRepository.uploadImageToStorage(
                            imageUri = pickedImageUri!!,
                            folderName = "daily_meals"
                        ) { uploadSuccess, result ->
                            if (uploadSuccess) {
                                imageUrl = result
                                finalizeSave(result)
                            } else {
                                posting = false
                                statusMessage = result
                            }
                        }
                    } else {
                        posting = false
                        statusMessage = "Please select an image or paste a URL"
                    }
                }
            },
            enabled = !posting && menuEn.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_green)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(if (posting) stringResource(id = R.string.uploading) else stringResource(id = R.string.post_meal))
        }

        statusMessage?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun FacilityUploadForm() {
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    var titleEn by rememberSaveable { mutableStateOf("") }
    var titleKn by rememberSaveable { mutableStateOf("") }
    var emoji by rememberSaveable { mutableStateOf("🏫") }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var posting by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pickedImageUri = uri
        statusMessage = if (uri != null) null else statusMessage
    }

    AdminCard {
        Button(
            onClick = { imagePicker.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_dark_blue)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(id = R.string.select_image))
        }
        pickedImageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.image_selected), style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text(stringResource(id = R.string.image_url)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(id = R.string.image_or_url_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = titleEn,
            onValueChange = { titleEn = it },
            label = { Text(stringResource(id = R.string.facility_title_english)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = titleKn,
            onValueChange = { titleKn = it },
            label = { Text(stringResource(id = R.string.facility_title_kannada)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = emoji,
            onValueChange = { emoji = it },
            label = { Text(stringResource(id = R.string.emoji)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    posting = true
                    statusMessage = null
                    val finalTitleKn = resolveKannadaText(titleEn, titleKn)
                    val finalizeSave: (String) -> Unit = { finalImageUrl ->
                        FirebaseContentRepository.saveFacility(
                            titleEnglish = titleEn.trim(),
                            titleKannada = finalTitleKn,
                            imageUrl = finalImageUrl,
                            emoji = emoji.ifBlank { "🏫" }
                        ) { success, message ->
                            posting = false
                            statusMessage = message
                            if (success) {
                                imageUrl = ""
                                pickedImageUri = null
                                titleEn = ""
                                titleKn = ""
                                emoji = "🏫"
                            }
                        }
                    }
                    val trimmedImageUrl = imageUrl.trim()
                    if (trimmedImageUrl.isNotBlank()) {
                        finalizeSave(trimmedImageUrl)
                    } else if (pickedImageUri != null) {
                        FirebaseContentRepository.uploadImageToStorage(
                            imageUri = pickedImageUri!!,
                            folderName = "facilities"
                        ) { uploadSuccess, result ->
                            if (uploadSuccess) {
                                imageUrl = result
                                finalizeSave(result)
                            } else {
                                posting = false
                                statusMessage = result
                            }
                        }
                    } else {
                        posting = false
                        statusMessage = "Please select an image or paste a URL"
                    }
                }
            },
            enabled = !posting && titleEn.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_dark_blue)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(if (posting) stringResource(id = R.string.uploading) else stringResource(id = R.string.post_facility))
        }

        statusMessage?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun StudentStarUploadForm() {
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var achievementEn by rememberSaveable { mutableStateOf("") }
    var achievementKn by rememberSaveable { mutableStateOf("") }
    var emoji by rememberSaveable { mutableStateOf("⭐") }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var posting by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pickedImageUri = uri
        statusMessage = if (uri != null) null else statusMessage
    }

    AdminCard {
        Button(
            onClick = { imagePicker.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_dark_blue)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(id = R.string.select_image))
        }
        pickedImageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.image_selected), style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text(stringResource(id = R.string.image_url)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(id = R.string.image_or_url_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id = R.string.student_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = achievementEn,
            onValueChange = { achievementEn = it },
            label = { Text(stringResource(id = R.string.achievement_english)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = achievementKn,
            onValueChange = { achievementKn = it },
            label = { Text(stringResource(id = R.string.achievement_kannada)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = emoji,
            onValueChange = { emoji = it },
            label = { Text(stringResource(id = R.string.emoji)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    posting = true
                    statusMessage = null
                    val finalAchievementKn = resolveKannadaText(achievementEn, achievementKn)
                    val finalizeSave: (String) -> Unit = { finalImageUrl ->
                        FirebaseContentRepository.saveStudentStar(
                            name = name.trim(),
                            achievementEnglish = achievementEn.trim(),
                            achievementKannada = finalAchievementKn,
                            imageUrl = finalImageUrl,
                            emoji = emoji.ifBlank { "⭐" }
                        ) { success, message ->
                            posting = false
                            statusMessage = message
                            if (success) {
                                imageUrl = ""
                                pickedImageUri = null
                                name = ""
                                achievementEn = ""
                                achievementKn = ""
                                emoji = "⭐"
                            }
                        }
                    }
                    val trimmedImageUrl = imageUrl.trim()
                    if (trimmedImageUrl.isNotBlank()) {
                        finalizeSave(trimmedImageUrl)
                    } else if (pickedImageUri != null) {
                        FirebaseContentRepository.uploadImageToStorage(
                            imageUri = pickedImageUri!!,
                            folderName = "student_stars"
                        ) { uploadSuccess, result ->
                            if (uploadSuccess) {
                                imageUrl = result
                                finalizeSave(result)
                            } else {
                                posting = false
                                statusMessage = result
                            }
                        }
                    } else {
                        posting = false
                        statusMessage = "Please select an image or paste a URL"
                    }
                }
            },
            enabled = !posting && name.isNotBlank() && achievementEn.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_purple)),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(if (posting) stringResource(id = R.string.uploading) else stringResource(id = R.string.post_star))
        }

        statusMessage?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun AdminCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = content
        )
    }
}

private suspend fun resolveKannadaText(englishText: String, existingKannadaText: String): String {
    val trimmedKannada = existingKannadaText.trim()
    if (trimmedKannada.isNotBlank()) return trimmedKannada

    val translated = GeminiTranslator.translateToKannada(englishText.trim())
    return translated?.takeIf { it.isNotBlank() } ?: englishText.trim()
}
