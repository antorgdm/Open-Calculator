package com.example.data.database

import kotlinx.coroutines.flow.Flow

/**
 * Repository layer abstracting calculation history database operations from ViewModels.
 */
class HistoryRepository(private val dao: CalculationHistoryDao) {
    val allHistory: Flow<List<CalculationHistoryEntity>> = dao.getAllHistory()
    val favorites: Flow<List<CalculationHistoryEntity>> = dao.getFavorites()

    fun filter(query: String, favoritesOnly: Boolean): Flow<List<CalculationHistoryEntity>> {
        val sanitizedQuery = query
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
            .trim()
        return dao.filterHistory(sanitizedQuery, favoritesOnly)
    }

    suspend fun insert(expression: String, result: String): Long {
        return dao.insert(
            CalculationHistoryEntity(
                expression = expression,
                result = result,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun setFavorite(id: Long, isFavorite: Boolean) {
        dao.setFavorite(id, isFavorite)
    }

    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
