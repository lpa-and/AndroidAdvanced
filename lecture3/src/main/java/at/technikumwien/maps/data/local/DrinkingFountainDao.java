package at.technikumwien.maps.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DrinkingFountainDao {

    @Query("SELECT * FROM drinking_fountains")
    List<DrinkingFountain> getAllDrinkingFountains();

    // @Query("SELECT fullname FROM drinking_fountains")
    // List<String> getDrinkingFountainNames();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DrinkingFountain... drinkingFountains);

    @Query("DELETE FROM drinking_fountains")
    void deleteAll();
}
