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

# FacilityScreen
fac_search = "var items by remember { mutableStateOf(emptyList<FacilityItem>()) }"
fac_rep = "var items by remember { mutableStateOf(emptyList<FacilityItem>()) }\n    var isLoading by remember { mutableStateOf(true) }"
text = text.replace(fac_search, fac_rep)

text = text.replace('items = it\n                errorMessage = null', 'items = it\n                isLoading = false\n                errorMessage = null')

fac_disp_search = """            items.forEach { item ->
                FacilityDetailCard(item, currentLanguage)
            }"""
fac_disp_rep = """            AnimatedVisibility(visible = isLoading, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    ShimmerCardItem()
                    ShimmerCardItem()
                }
            }
            AnimatedVisibility(visible = !isLoading, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    items.forEach { item ->
                        FacilityDetailCard(item, currentLanguage)
                    }
                }
            }"""
text = text.replace(fac_disp_search, fac_disp_rep)

# StarsScreen
star_search = "var items by remember { mutableStateOf(emptyList<StudentStar>()) }"
star_rep = "var items by remember { mutableStateOf(emptyList<StudentStar>()) }\n    var isLoading by remember { mutableStateOf(true) }"
text = text.replace(star_search, star_rep)

text = text.replace('items = it\n                errorMessage = null', 'items = it\n                isLoading = false\n                errorMessage = null')

star_disp_search = """            items.forEach { item ->
                StarDetailCard(item, currentLanguage)
            }"""
star_disp_rep = """            AnimatedVisibility(visible = isLoading, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    ShimmerCardItem()
                    ShimmerCardItem()
                }
            }
            AnimatedVisibility(visible = !isLoading, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    items.forEach { item ->
                        StarDetailCard(item, currentLanguage)
                    }
                }
            }"""
text = text.replace(star_disp_search, star_disp_rep)

# Delete old errorMessage blocks
import re
text = re.sub(r'errorMessage\?\.let \{.*?\}', '', text, flags=re.DOTALL)

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
