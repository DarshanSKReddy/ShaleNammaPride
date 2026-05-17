with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "r") as f:
    text = f.read()

if "import com.example.shale_nammapride.LocalSnackbar" not in text:
    text = text.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport com.example.shale_nammapride.LocalSnackbar")

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
