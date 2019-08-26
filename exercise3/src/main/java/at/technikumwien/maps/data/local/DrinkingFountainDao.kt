package at.technikumwien.maps.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

import at.technikumwien.maps.data.model.DrinkingFountain
import at.technikumwien.maps.data.model.DrinkingFountain.Companion.TABLE_NAME

@Dao
interface DrinkingFountainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinkingFountains: List<DrinkingFountain>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Transaction
    suspend fun refresh(drinkingFountains: List<DrinkingFountain>) {
        deleteAll()
        insert(drinkingFountains)
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun loadAll(): LiveData<List<DrinkingFountain>>

}
