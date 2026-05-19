with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "r") as f:
    text = f.read()

import re

# Add animateContentSize import if missing
if "import androidx.compose.animation.animateContentSize" not in text:
    text = text.replace("import androidx.compose.animation.AnimatedVisibility", 
                        "import androidx.compose.animation.AnimatedVisibility\nimport androidx.compose.animation.animateContentSize")

# Inject animateContentSize() into generic Modifiers in Cards
text = re.sub(r'Modifier\n\s*\.fillMaxWidth\(\)', r'Modifier\n        .fillMaxWidth()\n        .animateContentSize()', text)
text = re.sub(r'Modifier\.fillMaxWidth\(\)', r'Modifier.fillMaxWidth().animateContentSize()', text)

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
