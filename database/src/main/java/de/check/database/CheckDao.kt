package de.check.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.check.database.tables.Pool

@Dao
interface CheckDao {

    @Insert
    fun insertPool(pool: Pool)

    @Update
    fun updatePool(pool: Pool)

     @Delete
     fun removePool(pool: Pool)

     @Query("SELECT * FROM pools")
     fun getPools(): List<Pool>

}
