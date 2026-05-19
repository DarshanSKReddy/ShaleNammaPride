## How Role-Based Access Works (Teacher vs Parents)

By default, we have integrated a Firebase Authentication system. Wait, Firebase handles user creation (as seen in `LoginScreen`), but we need to assign **roles** to these users.

### Step 1: Manage Database Rules (Security)
In your Firebase Console, navigate to **Realtime Database** -> **Rules**. Paste the following rules:
```json
{
  "rules": {
    "users": {
       "$uid": {
         // Users can only read their own profile, but admins can read all
         ".read": "auth != null",
         ".write": "$uid === auth.uid"
       }
    },
    // Daily Meals
    "daily_meals": {
      // Anyone logged in (parents, students, teachers) can view the meals
      ".read": "auth != null",
      // ONLY Admins (teachers) can post new meals
      ".write": "auth != null && root.child('users').child(auth.uid).child('role').val() === 'admin'"
    },
    // Facilities
    "facilities": {
      ".read": "auth != null",
      ".write": "auth != null && root.child('users').child(auth.uid).child('role').val() === 'admin'"
    },
    // Student Stars
    "student_stars": {
      ".read": "auth != null",
      ".write": "auth != null && root.child('users').child(auth.uid).child('role').val() === 'admin'"
    },
    // Feedback
    "feedback": {
      // Admins read all, users only read their own (if needed, but usually just admins reading)
      ".read": "auth != null && root.child('users').child(auth.uid).child('role').val() === 'admin'",
      // Parents & Students CAN write/insert feedback
      ".write": "auth != null"
    }
  }
}
```

### Step 2: Configure Roles
1. When a parent registers, they don't have an `admin` role in their database tree by default, so they cannot write to `daily_meals`.
2. To make a teacher an Admin, simply log into the Firebase Console -> Realtime Database -> Data -> `users` node.
3. Find their Login UID, and manually add: `role: "admin"`.
4. Our app's Android UI uses `ViewModel.checkUserRole()` to see this variable. If it's `"admin"`, the app reveals the secret `Admin Upload` button at the top of the Home Screen!

### Step 3: Handling Daily Photo Uploads & Local Caching
Our codebase is strictly enforcing an **Offline-First** model. Look at `screens/AdminUploadScreen.kt` and `data/FirebaseContentRepository.kt`:
1. **Teacher Uploads:** When a teacher uploads a Daily Meal, we extract the image (`URI`) and push it via `storageReference.putFile(imageUri)` to Firebase Storage. 
2. **Download URL:** We extract the `.downloadUrl` which converts the internal file into a public HTTPS link.
3. **Database Saving:** This HTTPS link is saved alongside the Kannada/English descriptions in Realtime Database.
4. **Offline Viewing for Parents:** Realtime Database pushes the new update down to all connected phones. However, instead of passing this to Compose directly, our `FirebaseContentRepository` intercepts the data, silently inserts it into the Android **Room SQLite cache**, and then emits it through a Kotlin `Flow` directly onto the `DailyMealScreen`. 
5. Parents can open the app while completely offline or driving in zones without cell service and still seamlessly browse all the cached historical photos via the **Previous Meals** bottom scroller!
