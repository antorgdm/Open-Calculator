package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationHistoryDao {
    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<CalculationHistoryEntity>>

    @Query("SELECT * FROM calculation_history WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<CalculationHistoryEntity>>

    @Query("""
        SELECT * FROM calculation_history 
        WHERE (:favoritesOnly = 0 OR isFavorite = 1)
          AND (:query = '' OR expression LIKE '%' || :query || '%' ESCAPE '\' OR result LIKE '%' || :query || '%' ESCAPE '\')
        ORDER BY timestamp DESC
    """)
    fun filterHistory(query: String, favoritesOnly: Boolean): Flow<List<CalculationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CalculationHistoryEntity): Long

    @Update
    suspend fun update(item: CalculationHistoryEntity)

    @Query("DELETE FROM calculation_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM calculation_history")
    suspend fun clearAll()

    @Query("UPDATE calculation_history SET isFavorite = :isFav WHERE id = :id")
    suspend fun setFavorite(id: Long, isFav: Boolean)
}
