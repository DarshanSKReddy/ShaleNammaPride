package com.example.shale_nammapride.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// This represents a single Daily Meal post
@Entity(tableName = "daily_meals")
data class DailyMeal(
    @PrimaryKey
    val id: String = "",
    val date: String = "",
    val imageUrl: String = "",
    val menuEnglish: String = "",
    val menuKannada: String = "",
    val postedBy: String = ""
)

@Entity(tableName = "facilities")
data class FacilityItem(
    @PrimaryKey
    val id: String = "",
    val titleEnglish: String = "",
    val titleKannada: String = "",
    val imageUrl: String = "",
    val emoji: String = "🏫",
    val order: Int = 0
)

@Entity(tableName = "student_stars")
data class StudentStar(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val achievementEnglish: String = "",
    val achievementKannada: String = "",
    val imageUrl: String = "",
    val emoji: String = "⭐",
    val date: String = ""
)

// This represents a piece of Feedback
@Entity(tableName = "feedbacks")
data class Feedback(
    @PrimaryKey
    val id: String = "",
    val message: String = "",
    val sender: String = "Anonymous",
    val timestamp: Long = 0L
)