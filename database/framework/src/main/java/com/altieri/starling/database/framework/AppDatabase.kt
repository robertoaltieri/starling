package com.altieri.starling.database.framework

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AccountDBEntity::class],
    version = 1,
    exportSchema = true,
)

abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(lock = this) {
                INSTANCE ?: build(context).also { database ->
                    INSTANCE = database
                }
            }
        }

        private fun build(applicationContext: Context): AppDatabase =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "app-database"
            )
                .build()

    }

    abstract fun accountDao(): AccountDao
}

@Dao
interface AccountDao {

    @Query("SELECT * FROM Account WHERE accountType= '${AccountType.PRIMARY}'")
    suspend fun primaryAccount(): List<AccountDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(accounts: List<AccountDBEntity>)

    @Query("DELETE from Account")
    suspend fun clear()

}

private object AccountType {
    const val PRIMARY="PRIMARY"
}

@Entity(tableName = "Account", indices = [Index("accountUid")])
data class AccountDBEntity(
    @PrimaryKey
    @ColumnInfo(name = "accountUid") val accountUid: String,
    @ColumnInfo(name = "accountType") val accountType: String,
    @ColumnInfo(name = "defaultCategory") val defaultCategory: String,
    @ColumnInfo(name = "currency") val currency: String
)
