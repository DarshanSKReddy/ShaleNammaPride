package com.example.shale_nammapride.data

import android.net.Uri
import com.example.shale_nammapride.model.DailyMeal
import com.example.shale_nammapride.model.FacilityItem
import com.example.shale_nammapride.model.Feedback
import com.example.shale_nammapride.model.StudentStar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@javax.inject.Singleton
class FirebaseContentRepository @javax.inject.Inject constructor(
    private val roomDao: AppRoomDao
) {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

    private fun normalizedId(): String = database.push().key.orEmpty()

    fun submitFeedback(
        message: String,
        anonymousMode: Boolean,
        onResult: (Boolean, String) -> Unit
    ) {
        if (message.isBlank()) {
            onResult(false, "Please enter feedback first")
            return
        }

        val feedbackId = database.child("feedback").push().key
        if (feedbackId == null) {
            onResult(false, "Could not create feedback entry")
            return
        }

        val feedback = Feedback(
            id = feedbackId,
            message = message,
            sender = if (anonymousMode) "Anonymous" else (auth.currentUser?.email ?: "Registered User"),
            timestamp = System.currentTimeMillis()
        )

        database.child("feedback").child(feedbackId).setValue(feedback)
            .addOnSuccessListener { onResult(true, "Feedback sent successfully") }
            .addOnFailureListener { exception ->
                onResult(false, exception.localizedMessage ?: "Failed to send feedback")
            }
    }

    fun saveFacility(
        titleEnglish: String,
        titleKannada: String,
        imageUrl: String,
        emoji: String = "🏫",
        onResult: (Boolean, String) -> Unit
    ) {
        val id = normalizedId()
        if (id.isBlank()) {
            onResult(false, "Could not create facility entry")
            return
        }

        val item = FacilityItem(
            id = id,
            titleEnglish = titleEnglish,
            titleKannada = titleKannada,
            imageUrl = imageUrl,
            emoji = emoji,
            order = System.currentTimeMillis().toInt()
        )

        database.child("facilities").child(id).setValue(item)
            .addOnSuccessListener { onResult(true, "Facility saved successfully") }
            .addOnFailureListener { exception ->
                onResult(false, exception.localizedMessage ?: "Failed to save facility")
            }
    }

    fun saveStudentStar(
        name: String,
        achievementEnglish: String,
        achievementKannada: String,
        imageUrl: String,
        emoji: String = "⭐",
        onResult: (Boolean, String) -> Unit
    ) {
        val id = normalizedId()
        if (id.isBlank()) {
            onResult(false, "Could not create student star entry")
            return
        }

        val item = StudentStar(
            id = id,
            name = name,
            achievementEnglish = achievementEnglish,
            achievementKannada = achievementKannada,
            imageUrl = imageUrl,
            emoji = emoji,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        )

        database.child("student_stars").child(id).setValue(item)
            .addOnSuccessListener { onResult(true, "Student star saved successfully") }
            .addOnFailureListener { exception ->
                onResult(false, exception.localizedMessage ?: "Failed to save student star")
            }
    }

    fun saveDailyMeal(
        imageUrl: String,
        menuEnglish: String,
        menuKannada: String,
        postedBy: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val mealRef = database.child("daily_meals").child(date)

        mealRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                onResult(false, "Only one meal post is allowed per day")
                return@addOnSuccessListener
            }

            val meal = DailyMeal(
                id = date,
                date = date,
                imageUrl = imageUrl,
                menuEnglish = menuEnglish,
                menuKannada = menuKannada,
                postedBy = postedBy
            )

            mealRef.setValue(meal)
                .addOnSuccessListener { onResult(true, "Meal saved successfully") }
                .addOnFailureListener { exception ->
                    onResult(false, exception.localizedMessage ?: "Failed to save meal")
                }
        }.addOnFailureListener { exception ->
            onResult(false, exception.localizedMessage ?: "Failed to check existing meal")
        }
    }

    fun uploadImageToStorage(
        imageUri: Uri,
        folderName: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val fileName = "${System.currentTimeMillis()}.jpg"
        val imageRef = storage.child("admin_uploads/$folderName/$fileName")

        imageRef.putFile(imageUri)
            .continueWithTask { uploadTask ->
                if (!uploadTask.isSuccessful) {
                    throw uploadTask.exception ?: IllegalStateException("Image upload failed")
                }
                imageRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                onResult(true, uri.toString())
            }
            .addOnFailureListener { exception ->
                onResult(false, exception.localizedMessage ?: "Failed to upload image")
            }
    }

    fun listenToDailyMeals(
        onMealsChanged: (List<DailyMeal>) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            roomDao.getMeals().collect { meals ->
                withContext(Dispatchers.Main) { onMealsChanged(meals) }
            }
        }
        database.child("daily_meals")
            .orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val meals = snapshot.children.mapNotNull { it.getValue(DailyMeal::class.java) }
                    CoroutineScope(Dispatchers.IO).launch { roomDao.insertMeals(meals) }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    fun listenToFacilities(
        onItemsChanged: (List<FacilityItem>) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            roomDao.getFacilities().collect { items ->
                withContext(Dispatchers.Main) { onItemsChanged(items) }
            }
        }
        database.child("facilities")
            .orderByChild("order")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { it.getValue(FacilityItem::class.java) }
                    CoroutineScope(Dispatchers.IO).launch { roomDao.insertFacilities(items) }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    fun listenToStudentStars(
        onItemsChanged: (List<StudentStar>) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            roomDao.getStudentStars().collect { items ->
                withContext(Dispatchers.Main) { onItemsChanged(items) }
            }
        }
        database.child("student_stars")
            .orderByChild("date")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { it.getValue(StudentStar::class.java) }
                    CoroutineScope(Dispatchers.IO).launch { roomDao.insertStudentStars(items) }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    fun listenToFeedback(
        onFeedbackChanged: (List<Feedback>) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            roomDao.getFeedbacks().collect { items ->
                withContext(Dispatchers.Main) { onFeedbackChanged(items) }
            }
        }
        database.child("feedback")
            .orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { it.getValue(Feedback::class.java) }
                    CoroutineScope(Dispatchers.IO).launch { roomDao.insertFeedbacks(items) }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }
}