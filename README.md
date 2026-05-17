# Shale Namma Pride 🏫✨

## 📖 Problem Statement
Improving visibility, accountability, and communication in local schools. "Shale Namma Pride" is a dedicated Android application designed to bridge the gap between school administration, students, and parents. It provides a transparent platform to showcase school facilities, daily meals, track student feedback, and allow admins to manage school updates efficiently.

## 🚀 Features
- **Secure Authentication:** Role-based access for Admins and regular users (Students/Parents) using Firebase.
- **Multilingual Support:** Intelligent translations powered by Google Gemini to break down language barriers.
- **AI Summaries:** Automated summaries of student/parent feedback for admins using Google Gemini API.
- **Offline-First Architecture:** Local data caching via Android Room and Flow for seamless usage without internet connectivity.
- **Modern UI/UX:** Shimmer loading effects, screen transition animations, and snackbar error handling built entirely in Jetpack Compose.
- **Facility Showcase:** View school infrastructure (classrooms, labs, library, etc.).
- **Meal Tracking:** Daily updates on school meals provided to students.
- **Feedback System:** A dedicated portal for users to submit feedback and suggestions.
- **Admin Dashboard:** Admins can easily upload and manage content, notices, and track interactions.

## 💻 Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Animations, State Hoisting)
- **Backend & Auth:** Firebase (Authentication, Realtime Database)
- **Local Database:** Android Room (SQLite cache)
- **AI Integration:** Google Generative AI (Gemini) API
- **Architecture:** MVVM Design Pattern with Dagger Hilt (Dependency Injection)

## ⚙️ Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/DarshanSKReddy/ShaleNammaPride.git
   ```
2. Open the project in **Android Studio** (Ladybug or newer recommended).
3. Let Gradle sync and download all necessary dependencies.
4. Set up Firebase:
   - Add your `google-services.json` file in the `app/` directory (ensure Firebase Authentication and Realtime Database are enabled).
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
│   ├── data/       # Repositories, Room DB DAOs, Firebase logic, Gemini Integration
│   ├── model/      # Data classes and structures
│   ├── ui/theme/   # Jetpack Compose theming, typography, colors, animations, shimmers
│   └── view/       # Compose Screens (Home, Login, Admin, Feedback)
└── res/            # Resources (Drawables, Values, Layouts)
```

## 🔮 Future Improvements
- Implement push notifications for urgent school announcements.
- Add an attendance tracking module for students.

---
*Built with passion to elevate the pride of our schools.*
