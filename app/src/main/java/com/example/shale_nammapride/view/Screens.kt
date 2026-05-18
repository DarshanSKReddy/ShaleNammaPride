package com.example.shale_nammapride.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.animateContentSize
import com.example.shale_nammapride.LocalSnackbar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shale_nammapride.R
import com.example.shale_nammapride.data.FirebaseContentRepository
import com.example.shale_nammapride.model.DailyMeal
import com.example.shale_nammapride.model.FacilityItem
import com.example.shale_nammapride.model.StudentStar

@Composable
fun DailyMealScreen(viewModel: MainViewModel, currentLanguage: String, onBackPressed: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    var meals by remember { mutableStateOf(emptyList<DailyMeal>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbar = LocalSnackbar.current
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbar.showSnackbar(errorMessage!!)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.repository.listenToDailyMeals(
            onMealsChanged = {
                meals = it
                errorMessage = null
            },
            onError = { errorMessage = it }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.light_gray))) {
        HeaderBar(
            title = localizedText(currentLanguage, "Daily Meal", "ದಿನಸಿ ಊಟ"),
            subtitle = localizedText(currentLanguage, "Mid-day Meal Updates", "ಮಧ್ಯಾಹ್ನ ಊಟ ನವೀಕರಣ"),
            backgroundColor = colorResource(id = R.color.light_yellow_bg),
            onBackPressed = onBackPressed
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            HeroAssetCard(
                spec = AssetSpec(
                    fileNameWithoutExtension = "meal_placeholder",
                    folderHint = "res/drawable",
                    fallbackEmoji = "🍛"
                ),
                overlayTitle = stringResource(id = R.string.daily_meal),
                overlaySubtitle = localizedText(currentLanguage, "Mid-day Meal Updates", "ಮಧ್ಯಾಹ್ನ ಊಟ ನವೀಕರಣ"),
                heightDp = 180.dp
            )
            Spacer(modifier = Modifier.height(12.dp))
            MealDetailCard(meals.firstOrNull(), currentLanguage)
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            NutritionCard(currentLanguage)
            NoteCard(currentLanguage)
            PreviousMealsRow(currentLanguage, meals.drop(1))
        }
    }
}

@Composable
private fun MealDetailCard(meal: DailyMeal?, currentLanguage: String) {
    val primaryMenu = if (currentLanguage == "kn") meal?.menuKannada else meal?.menuEnglish
    val secondaryMenu = if (currentLanguage == "kn") meal?.menuEnglish else meal?.menuKannada
    val menuTitle = if (primaryMenu.isNullOrBlank()) {
        secondaryMenu?.takeIf { it.isNotBlank() } ?: localizedText(currentLanguage, "Today's Menu", "ಇಂದಿನ ಮೆನು")
    } else {
        primaryMenu
    }

    val imageUrl = meal?.imageUrl.orEmpty()
    val imageSpec = AssetSpec(
        fileNameWithoutExtension = "meal_placeholder",
        folderHint = "res/drawable",
        fallbackEmoji = "🍛"
    )

    val dateLabel = meal?.date?.ifBlank { localizedDateLabel(currentLanguage) } ?: localizedDateLabel(currentLanguage)

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize().padding(bottom = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().animateContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📅", fontSize = 20.sp)
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = localizedText(currentLanguage, "Today's Meal", "ಇಂದಿನ ಊಟ"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primary_green)
                        )
                        Text(dateLabel, fontSize = 10.sp, color = colorResource(id = R.color.dark_text))
                    }
                }
                Surface(shape = RoundedCornerShape(6.dp), color = colorResource(id = R.color.light_green_bg)) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("✓", fontSize = 12.sp, color = colorResource(id = R.color.primary_green))
                        Text(
                            text = localizedText(currentLanguage, "Updated for today", "ಇಂದಿಗೆ ನವೀಕೃತಗೊಂಡಿದೆ"),
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.primary_green),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(colorResource(id = R.color.light_green_bg), shape = RoundedCornerShape(8.dp))
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Daily meal image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    DrawableAssetOrHint(
                        spec = imageSpec,
                        modifier = Modifier.fillMaxSize(),
                        showHint = true
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().animateContentSize().padding(top = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_gray))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
                        Text("🥄", fontSize = 15.sp)
                        Text(
                            text = localizedText(currentLanguage, "Today's Menu", "ಇಂದಿನ ಮೆನು"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primary_dark_blue),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Text(
                        text = menuTitle,
                        fontSize = 11.sp,
                        color = colorResource(id = R.color.dark_text)
                    )
                }
            }
        }
    }
}

@Composable
private fun NutritionCard(currentLanguage: String) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize().padding(bottom = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_blue_card))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⭐", fontSize = 18.sp)
                Text(
                    text = localizedText(currentLanguage, "Nutrition Info", "ಪೌಷ್ಟಿಕ ಮಾಹಿತಿ"),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primary_dark_blue),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = localizedText(currentLanguage, "Healthy meal for a strong body and bright future!", "ಮಾನಸಿಕ ಶರೀರ ಮತ್ತು ಪ್ರಕಾಶಮಾನ ಭವಿಷ್ಯತ್ಕ್ಕೆ ಆರೋಗ್ಯಕರ ಊಟ!"),
                fontSize = 11.sp,
                color = colorResource(id = R.color.dark_text),
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun NoteCard(currentLanguage: String) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize().padding(bottom = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_yellow_bg))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔒", fontSize = 18.sp)
                Text(
                    text = localizedText(currentLanguage, "Note", "ಟಿಪ್ಪಣಿ"),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primary_dark_blue),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = localizedText(currentLanguage, "Only one meal update is allowed per day.", "ಪ್ರತಿದಿನ ಕೇವಲ ಒಂದು ಊಟ ನವೀಕರಣವನ್ನು ಅನುಮತಿಸಲಾಗುತ್ತದೆ."),
                fontSize = 11.sp,
                color = colorResource(id = R.color.dark_text),
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun PreviousMealsRow(currentLanguage: String, previousMeals: List<DailyMeal>) {
    Text(
        text = localizedText(currentLanguage, "Previous Meals", "ಹಿಂದಿನ ಊಟ"),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.primary_dark_blue),
        modifier = Modifier.padding(bottom = 12.dp)
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 16.dp)) {
        items(if (previousMeals.isNotEmpty()) previousMeals else List(4) { DailyMeal(date = "0${4 - it} May") }) { meal ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(84.dp).background(colorResource(id = R.color.light_green_bg), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🍲", fontSize = 32.sp)
                }
                Text(
                    text = meal.date,
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.dark_text),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
    }
}

@Composable
fun FacilityScreen(viewModel: MainViewModel, currentLanguage: String, onBackPressed: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    var facilities by remember { mutableStateOf(emptyList<FacilityItem>()) }

    LaunchedEffect(Unit) {
        viewModel.repository.listenToFacilities(
            onItemsChanged = { facilities = it },
            onError = { facilities = emptyList() }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.light_gray))) {
        HeaderBar(
            title = localizedText(currentLanguage, "Facilities", "ಸೌಕರ್ಯಗಳು"),
            subtitle = localizedText(currentLanguage, "Explore our school facilities and gallery", "ನಮ್ಮ ಶಾಲೆ ಸೌಕರ್ಯ ಮತ್ತು ಗ್ಯಾಲರಿ ಅನ್ವೇಷಿಸಿ"),
            backgroundColor = colorResource(id = R.color.light_blue_card),
            onBackPressed = onBackPressed
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
            HeroAssetCard(
                spec = AssetSpec(
                    fileNameWithoutExtension = "school_vector",
                    folderHint = "res/drawable",
                    fallbackEmoji = "🏫"
                ),
                overlayTitle = stringResource(id = R.string.facilities),
                overlaySubtitle = localizedText(currentLanguage, "Explore our school facilities and gallery", "ನಮ್ಮ ಶಾಲೆ ಸೌಕರ್ಯ ಮತ್ತು ಗ್ಯಾಲರಿ ಅನ್ವೇಷಿಸಿ"),
                heightDp = 180.dp,
                contentScale = ContentScale.Crop,
                showOverlayScrim = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            val displayItems = if (facilities.isNotEmpty()) {
                facilities
            } else {
                listOf(
                    FacilityItem(titleEnglish = "Smart Classroom", titleKannada = "ಸ್ಮಾರ್ಟ್ ಕ್ಲಾಸ್ ರೂಮ್", emoji = "🖥️"),
                    FacilityItem(titleEnglish = "Science Lab", titleKannada = "ವಿಜ್ಞಾನ ಪ್ರಯೋಗಾಲಯ", emoji = "🔬"),
                    FacilityItem(titleEnglish = "Library", titleKannada = "ಗ್ರಂಥಾಲಯ", emoji = "📚"),
                    FacilityItem(titleEnglish = "Toilets", titleKannada = "ಶೌಚಾಲಯ", emoji = "🚻")
                )
            }

            displayItems.forEachIndexed { index, item ->
                val localImageName = when (index) {
                    0 -> "facility_classroom"
                    1 -> "facility_lab"
                    2 -> "facility_library"
                    else -> "facility_toilet"
                }
                val title = if (currentLanguage == "kn") {
                    if (item.titleKannada.isNotBlank()) item.titleKannada else item.titleEnglish
                } else {
                    if (item.titleEnglish.isNotBlank()) item.titleEnglish else item.titleKannada
                }

                Card(
                    modifier = Modifier.fillMaxWidth().animateContentSize().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Box(
                            modifier = Modifier.fillMaxWidth().animateContentSize().height(172.dp).background(colorResource(id = R.color.light_blue_card), shape = RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.titleEnglish,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                DrawableAssetOrHint(
                                    spec = AssetSpec(
                                        fileNameWithoutExtension = localImageName,
                                        folderHint = "res/drawable-nodpi",
                                        fallbackEmoji = item.emoji
                                    ),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Text(
                            text = title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primary_dark_blue),
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StarsScreen(viewModel: MainViewModel, currentLanguage: String, onBackPressed: () -> Unit = {}) {
    var studentStars by remember { mutableStateOf(emptyList<StudentStar>()) }

    LaunchedEffect(Unit) {
        viewModel.repository.listenToStudentStars(
            onItemsChanged = { studentStars = it },
            onError = { studentStars = emptyList() }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.light_gray))) {

        HeaderBar(
            title = localizedText(currentLanguage, "Student Stars", "ವಿದ್ಯಾರ್ಥಿ ನಕ್ಷತ್ರಗಳು"),
            subtitle = localizedText(currentLanguage, "Celebrate achievements and winners", "ಸಾಧನೆ ಮತ್ತು ವಿಜೇತೆಯವರನ್ನು ಆದರಿಸಿ"),
            backgroundColor = colorResource(id = R.color.light_purple_bg),
            onBackPressed = onBackPressed
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HeroAssetCard(
                spec = AssetSpec(
                    fileNameWithoutExtension = "trophy_icon",
                    folderHint = "res/drawable",
                    fallbackEmoji = "🏆"
                ),
                overlayTitle = stringResource(id = R.string.student_stars),
                overlaySubtitle = localizedText(currentLanguage, "Celebrate achievements and winners", "ಸಾಧನೆ ಮತ್ತು ವಿಜೇತೆಯವರನ್ನು ಆದರಿಸಿ"),
                heightDp = 180.dp,
                contentScale = ContentScale.Fit,
                showOverlayScrim = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = localizedText(currentLanguage, "Star of the Week", "ವಾರದ ನಕ್ಷತ್ರ"),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primary_dark_blue),
                modifier = Modifier.padding(bottom = 10.dp)
            )
            val displayStars = if (studentStars.isNotEmpty()) {
                studentStars
            } else {
                listOf(
                    StudentStar(name = "Pooja K", achievementEnglish = "Won District Level Drawing Competition", achievementKannada = "ಜಿಲ್ಲಾ ಮಟ್ಟದ ಚಿತ್ರಕಲೆ ಸ್ಪರ್ಧೆಯಲ್ಲಿ ಗೆದ್ದಿದ್ದಾರೆ", emoji = "👧")
                )
            }

            displayStars.forEach { star ->
                val achievement = if (currentLanguage == "kn") {
                    if (star.achievementKannada.isNotBlank()) star.achievementKannada else star.achievementEnglish
                } else {
                    if (star.achievementEnglish.isNotBlank()) star.achievementEnglish else star.achievementKannada
                }

                Card(
                    modifier = Modifier.fillMaxWidth().animateContentSize().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().animateContentSize().padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(112.dp).background(colorResource(id = R.color.light_purple_bg), shape = RoundedCornerShape(50.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (star.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = star.imageUrl,
                                    contentDescription = star.name,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                DrawableAssetOrHint(
                                    spec = AssetSpec(
                                        fileNameWithoutExtension = "student_placeholder",
                                        folderHint = "res/drawable",
                                        fallbackEmoji = star.emoji
                                    ),
                                    modifier = Modifier.fillMaxSize(),
                                    showHint = true
                                )
                            }
                        }
                        Text(
                            text = star.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primary_dark_blue),
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = achievement,
                            fontSize = 11.sp,
                            color = colorResource(id = R.color.dark_text),
                            modifier = Modifier.padding(top = 6.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackScreen(viewModel: MainViewModel, currentLanguage: String, onBackPressed: () -> Unit = {}) {
    var feedbackText by rememberSaveable { mutableStateOf("") }
    var anonymousMode by rememberSaveable { mutableStateOf(false) }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var sending by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.light_gray))) {
        HeaderBar(
            title = localizedText(currentLanguage, "Feedback", "ಮಾಹಿತಿ ಸಂಗ್ರಹ"),
            subtitle = localizedText(currentLanguage, "Share your suggestions and ideas", "ನಿಮ್ಮ ಸಲಹೆ ಮತ್ತು ವಿಚಾರಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ"),
            backgroundColor = colorResource(id = R.color.light_orange_bg),
            onBackPressed = onBackPressed
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HeroAssetCard(
                spec = AssetSpec(
                    fileNameWithoutExtension = "feedback_banner",
                    folderHint = "res/drawable",
                    fallbackEmoji = "💬"
                ),
                overlayTitle = localizedText(currentLanguage, "Feedback", "ಮಾಹಿತಿ ಸಂಗ್ರಹ"),
                overlaySubtitle = localizedText(currentLanguage, "Share your suggestions and ideas", "ನಿಮ್ಮ ಸಲಹೆ ಮತ್ತು ವಿಚಾರಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ"),
                heightDp = 220.dp,
                contentScale = ContentScale.Fit,
                showOverlayScrim = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth().animateContentSize().padding(bottom = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().animateContentSize().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔒", fontSize = 18.sp)
                        Text(
                            text = localizedText(currentLanguage, "Anonymous Mode", "ಅನಾಮಧೇಯ ಮೋಡ್"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.primary_dark_blue),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Switch(checked = anonymousMode, onCheckedChange = { anonymousMode = it })
                }
            }

            TextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                modifier = Modifier.fillMaxWidth().animateContentSize().height(170.dp).padding(bottom = 16.dp),
                placeholder = { Text(localizedText(currentLanguage, "Write a suggestion for the school committee...", "ಶಾಲಾ ಸಮಿತಿಗೆ ಸಲಹೆ ಬರೆಯಿರಿ...")) },
                shape = RoundedCornerShape(18.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Button(
                onClick = {
                    sending = true
                    statusMessage = null
                    viewModel.submitFeedback(
                        message = feedbackText.trim(),
                        anonymous = anonymousMode
                    ) { success, message ->
                        sending = false
                        statusMessage = message
                        if (success) {
                            feedbackText = ""
                        }
                    }
                },
                enabled = !sending && feedbackText.isNotBlank(),
                modifier = Modifier.fillMaxWidth().animateContentSize().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_orange)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (sending) localizedText(currentLanguage, "Uploading...", "ಅಪ್‌ಲೋಡ್ ಆಗುತ್ತಿದೆ...") else localizedText(currentLanguage, "Send Feedback", "ಮಾಹಿತಿ ಸಂಗ್ರಹ ಕಳುಹಿಸಿ"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            statusMessage?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 12.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun HeaderBar(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    onBackPressed: () -> Unit
) {
    val isCompactScreen = LocalConfiguration.current.screenWidthDp < 390

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = if (isCompactScreen) 10.dp else 16.dp, vertical = if (isCompactScreen) 10.dp else 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().animateContentSize(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = if (isCompactScreen) 21.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primary_dark_blue)
                )
                Text(
                    text = subtitle,
                    fontSize = if (isCompactScreen) 11.sp else 12.sp,
                    color = colorResource(id = R.color.dark_text),
                    maxLines = 1
                )
            }
        }
    }
}

private fun localizedDateLabel(currentLanguage: String): String {
    return if (currentLanguage == "kn") {
        "2025 ಮೇ 04, ಭಾನುವಾರ"
    } else {
        "04 May 2025, Sunday"
    }
}