package com.example.shale_nammapride.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shale_nammapride.model.DailyMeal
import com.example.shale_nammapride.model.FacilityItem
import com.example.shale_nammapride.model.Feedback
import com.example.shale_nammapride.model.StudentStar

@Database(
    entities = [DailyMeal::class, FacilityItem::class, StudentStar::class, Feedback::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appRoomDao(): AppRoomDao
}
