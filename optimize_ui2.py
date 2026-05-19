with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "r") as f:
    text = f.read()

import re

# Add animateContentSize import if missing
text = text.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport androidx.compose.animation.animateContentSize")

# Inject animateContentSize() into generic Modifiers in Cards
text = re.sub(r'Modifier\.fillMaxWidth\(\)', r'Modifier.fillMaxWidth().animateContentSize()', text)

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
