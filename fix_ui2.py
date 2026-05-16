import os

def fix_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # In UI files, change viewModel.xxx to viewModel.repository.xxx for missing ones:
    methods = ['saveDailyMeal', 'uploadImageToStorage', 'saveFacility', 'saveStudentStar', 'listenToDailyMeals', 'listenToFacilities', 'listenToStudentStars']
    for m in methods:
        content = content.replace(f'viewModel.{m}', f'viewModel.repository.{m}')

    with open(filepath, 'w') as f:
        f.write(content)

fix_file('app/src/main/java/com/example/shale_nammapride/view/AdminUploadScreen.kt')
fix_file('app/src/main/java/com/example/shale_nammapride/view/Screens.kt')

# Also, expose repository publicly in MainViewModel
with open('app/src/main/java/com/example/shale_nammapride/view/MainViewModel.kt', 'r') as f:
    vm_content = f.read()

vm_content = vm_content.replace('private val repository: FirebaseContentRepository', 'val repository: FirebaseContentRepository')
with open('app/src/main/java/com/example/shale_nammapride/view/MainViewModel.kt', 'w') as f:
    f.write(vm_content)
