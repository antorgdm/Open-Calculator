package com.example.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration definitions for incremental schema updates during application development.
 */
object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_calculation_history_timestamp` ON `calculation_history` (`timestamp`)"
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE `calculation_history` ADD COLUMN `isFavorite` INTEGER NOT NULL DEFAULT 0"
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_calculation_history_isFavorite` ON `calculation_history` (`isFavorite`)"
            )
        }
    }

    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3
    )
}
