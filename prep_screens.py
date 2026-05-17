import re

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "r") as f:
    text = f.read()

# Fix DailyMealScreen
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

# SnackBars
snack_rep = """
    val snackbar = LocalSnackbar.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            scope.launch { snackbar.showSnackbar(errorMessage!!) }
        }
    }
"""
text = text.replace('errorMessage?.let {', snack_rep + "    // ")


with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
