import re

with open('app/src/main/java/com/example/shale_nammapride/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('import androidx.compose.runtime.*', 'import androidx.compose.runtime.*\nimport androidx.hilt.navigation.compose.hiltViewModel\nimport com.example.shale_nammapride.view.MainViewModel')
content = content.replace('private fun AppRoot() {', 'private fun AppRoot(viewModel: MainViewModel = hiltViewModel()) {')
content = content.replace('fun AppNavigationWithHome(', 'fun AppNavigationWithHome(viewModel: MainViewModel, ')
content = content.replace('AppNavigationWithHome(', 'AppNavigationWithHome(viewModel = viewModel, ')
content = content.replace('AdminUploadScreen(', 'AdminUploadScreen(viewModel = viewModel, ')
content = content.replace('DailyMealScreen(', 'DailyMealScreen(viewModel = viewModel, ')
content = content.replace('FacilityScreen(', 'FacilityScreen(viewModel = viewModel, ')
content = content.replace('StarsScreen(', 'StarsScreen(viewModel = viewModel, ')
content = content.replace('FeedbackScreen(', 'FeedbackScreen(viewModel = viewModel, ')

with open('app/src/main/java/com/example/shale_nammapride/MainActivity.kt', 'w') as f:
    f.write(content)

with open('app/src/main/java/com/example/shale_nammapride/view/Screens.kt', 'r') as f:
    s_content = f.read()
    
# Need to add viewModel param to screens inside Screens.kt as well
s_content = s_content.replace('fun DailyMealScreen(', 'fun DailyMealScreen(viewModel: MainViewModel, ')
s_content = s_content.replace('fun FacilityScreen(', 'fun FacilityScreen(viewModel: MainViewModel, ')
s_content = s_content.replace('fun StarsScreen(', 'fun StarsScreen(viewModel: MainViewModel, ')
s_content = s_content.replace('fun FeedbackScreen(', 'fun FeedbackScreen(viewModel: MainViewModel, ')

with open('app/src/main/java/com/example/shale_nammapride/view/Screens.kt', 'w') as f:
    f.write(s_content)
