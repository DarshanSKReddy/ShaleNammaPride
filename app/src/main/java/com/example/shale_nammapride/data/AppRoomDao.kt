package com.example.shale_nammapride.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shale_nammapride.model.DailyMeal
import com.example.shale_nammapride.model.FacilityItem
import com.example.shale_nammapride.model.Feedback
import com.example.shale_nammapride.model.StudentStar
import kotlinx.coroutines.flow.Flow

@Dao
interface AppRoomDao {
    @Query("SELECT * FROM daily_meals ORDER BY date DESC")
    fun getMeals(): Flow<List<DailyMeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<DailyMeal>)

    @Query("SELECT * FROM facilities ORDER BY `order` ASC")
    fun getFacilities(): Flow<List<FacilityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFacilities(facilities: List<FacilityItem>)

    @Query("SELECT * FROM student_stars ORDER BY date DESC")
    fun getStudentStars(): Flow<List<StudentStar>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentStars(stars: List<StudentStar>)

    @Query("SELECT * FROM feedbacks ORDER BY timestamp DESC")
    fun getFeedbacks(): Flow<List<Feedback>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedbacks(feedbacks: List<Feedback>)
}
