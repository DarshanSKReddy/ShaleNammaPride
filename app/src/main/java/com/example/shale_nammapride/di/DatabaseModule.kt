package com.example.shale_nammapride.di

import android.content.Context
import androidx.room.Room
import com.example.shale_nammapride.data.AppDatabase
import com.example.shale_nammapride.data.AppRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shale_namma_pride_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAppRoomDao(database: AppDatabase): AppRoomDao {
        return database.appRoomDao()
    }
}
