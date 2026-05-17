with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "r") as f:
    text = f.read()

import re

# Add snackbar handling to the three specific screens via explicit replacement
snackbar_eff = """    val snackbar = LocalSnackbar.current
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbar.showSnackbar(errorMessage!!)
        }
    }
"""

text = text.replace('    var errorMessage by remember { mutableStateOf<String?>(null) }\n\n    LaunchedEffect(Unit)', '    var errorMessage by remember { mutableStateOf<String?>(null) }\n' + snackbar_eff + '\n    LaunchedEffect(Unit)')

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
