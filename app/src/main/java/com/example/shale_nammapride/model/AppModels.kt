package com.example.shale_nammapride.model

// This represents a single Daily Meal post
data class DailyMeal(
    val id: String = "",
    val date: String = "",
    val imageUrl: String = "",
    val menuEnglish: String = "",
    val menuKannada: String = "",
    val postedBy: String = ""
)

data class FacilityItem(
    val id: String = "",
    val titleEnglish: String = "",
    val titleKannada: String = "",
    val imageUrl: String = "",
    val emoji: String = "🏫",
    val order: Int = 0
)

data class StudentStar(
    val id: String = "",
    val name: String = "",
    val achievementEnglish: String = "",
    val achievementKannada: String = "",
    val imageUrl: String = "",
    val emoji: String = "⭐",
    val date: String = ""
)

// This represents a piece of Feedback
data class Feedback(
    val id: String = "",
    val message: String = "",
    val sender: String = "Anonymous",
    val timestamp: Long = 0L
)