import os
import re

def fix_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Find the main composable and add viewModel if not there
    if 'AdminUploadScreen(' in content and 'viewModel: MainViewModel' not in content:
        content = content.replace('fun AdminUploadScreen(', 'fun AdminUploadScreen(\n    viewModel: MainViewModel,\n')
        # Also need to import MainViewModel
        if 'import com.example.shale_nammapride.view.MainViewModel' not in content:
            content = content.replace('import androidx.compose', 'import com.example.shale_nammapride.view.MainViewModel\nimport androidx.compose', 1)

    if 'FirebaseContentRepository.' in content:
        content = content.replace('FirebaseContentRepository.', 'viewModel.')
        
    with open(filepath, 'w') as f:
        f.write(content)

fix_file('app/src/main/java/com/example/shale_nammapride/view/AdminUploadScreen.kt')
fix_file('app/src/main/java/com/example/shale_nammapride/view/Screens.kt')
