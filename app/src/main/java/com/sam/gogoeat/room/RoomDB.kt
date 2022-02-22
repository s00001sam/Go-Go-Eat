package com.sam.gogoeat.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sam.gogoeat.data.GogoPlace

@Database(
    entities = [GogoPlace::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDB: RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var instance: RoomDB? = null
        private val LOCK = Any()

        fun getInstance(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                "gogoeat_room_db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}