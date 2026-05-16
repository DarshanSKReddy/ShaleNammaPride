import re

with open('app/src/main/java/com/example/shale_nammapride/MainActivity.kt', 'r') as f:
    content = f.read()

# Add hiltViewModel import
if 'import androidx.hilt.navigation.compose.hiltViewModel' not in content:
    content = content.replace('import androidx.compose', 'import androidx.hilt.navigation.compose.hiltViewModel\nimport com.example.shale_nammapride.view.MainViewModel\nimport androidx.compose', 1)

# AppRoot needs MainViewModel
content = content.replace('private fun AppRoot() {', 'private fun AppRoot(viewModel: MainViewModel = hiltViewModel()) {')

# AppNavigationWithHome needs MainViewModel
content = content.replace('fun AppNavigationWithHome(', 'fun AppNavigationWithHome(\n    viewModel: MainViewModel,\n')

# Pass viewModel from AppRoot to AppNavigationWithHome
content = content.replace('AppNavigationWithHome(', 'AppNavigationWithHome(\n            viewModel = viewModel,')

# Add missing viewModel parameter to AdminUploadScreen, DailyMealScreen, FacilityScreen, StarsScreen, FeedbackScreen
content = content.replace('composable("admin") { AdminUploadScreen(', 'composable("admin") { AdminUploadScreen(viewModel = viewModel, ')
# Actually only AdminUploadScreen and Screens.kt (which has DailyMealScreen, etc.) use ViewModel?
for screen in ['DailyMealScreen', 'FacilityScreen', 'StarsScreen', 'FeedbackScreen']:
    content = content.replace(f'composable("{screen.lower()[:4]}.*?") {{ {screen}(', f'=>')
    content = re.sub(fr'composable\(".*?"\) {{ {screen}\(', f'composable("{screen.lower()}") {{ {screen}(viewModel = viewModel, ', content)

# I can just do a regex replace
screens = ['DailyMealScreen', 'FacilityScreen', 'StarsScreen', 'FeedbackScreen']
for s in screens:
    content = re.sub(r'(composable\(".*?"\)\s*\{\s*' + s + r'\()', r'\1viewModel = viewModel, ', content)

with open('app/src/main/java/com/example/shale_nammapride/MainActivity.kt', 'w') as f:
    f.write(content)

