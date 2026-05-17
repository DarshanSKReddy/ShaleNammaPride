import re

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

with open("app/src/main/java/com/example/shale_nammapride/view/Screens.kt", "w") as f:
    f.write(text)
