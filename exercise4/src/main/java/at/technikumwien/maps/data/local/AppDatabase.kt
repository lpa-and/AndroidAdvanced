package at.technikumwien.maps.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

import at.technikumwien.maps.data.model.DrinkingFountain

@Database(entities = [DrinkingFountain::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun drinkingFountainDao(): DrinkingFountainDao

}
