# Shale Namma Pride 🏫✨

## 📖 Problem Statement
Improving visibility, accountability, and communication in local schools. "Shale Namma Pride" is a dedicated Android application designed to bridge the gap between school administration, students, and parents. It provides a transparent platform to showcase school facilities, daily meals, track student feedback, and allow admins to manage school updates efficiently.

## 🚀 Features
- **Secure Authentication:** Role-based access for Admins and regular users (Students/Parents) using Firebase.
- **Multilingual Support:** Intelligent translations powered by Google Gemini to break down language barriers.
- **Facility Showcase:** View school infrastructure (classrooms, labs, library, etc.).
- **Meal Tracking:** Daily updates on school meals provided to students.
- **Feedback System:** A dedicated portal for users to submit feedback and suggestions.
- **Admin Dashboard:** Admins can easily upload and manage content, notices, and track interactions.

## 💻 Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Backend & Auth:** Firebase (Authentication, Storage, Analytics)
- **AI Integration:** Google Gemini API for translations
- **Architecture:** MVVM Design Pattern

## ⚙️ Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/DarshanSKReddy/ShaleNammaPride.git
   ```
2. Open the project in **Android Studio** (Ladybug or newer recommended).
3. Let Gradle sync and download all necessary dependencies.
4. Set up Firebase:
   - Add your `google-services.json` file in the `app/` directory (ensure Firebase Authentication and Firestore are enabled).
5. Set up Gemini API:
   - Provide your Gemini API key in `local.properties` or the appropriate environment variable as needed by the app.

## 🏃 Run Command
To build and run the application directly from the terminal (Ensure you have an emulator running or device connected):
```bash
./gradlew installDebug
```
Alternatively, click the **Run ('app')** button `▶` in Android Studio.

## 📸 Screenshots / Demo
*(Add your screenshots or video demo links here. You can upload them to the repository and link them below:)*
- [Demo Video](link-to-video)
- [Screenshots folder](./app/src/main/res/drawable/)

## 📂 Folder Structure
```
app/src/main/
├── java/com/example/shale_nammapride/
│   ├── data/       # Repositories, Firebase logic, Gemini Integration
│   ├── model/      # Data classes and structures
│   ├── ui/theme/   # Jetpack Compose theming, typography, colors
│   └── view/       # Compose Screens (Home, Login, Admin, Feedback)
└── res/            # Resources (Drawables, Values, Layouts)
```

## 🔮 Future Improvements
- Implement push notifications for urgent school announcements.
- Add an attendance tracking module for students.
- Expand the AI capabilities for automated feedback summarization.

---
*Built with passion to elevate the pride of our schools.*
