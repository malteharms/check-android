package de.check.database

import androidx.room.Dao
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

     @Query("DELETE FROM pools WHERE id = :id")
     fun removePool(id: Int)

     @Query("SELECT * FROM pools")
     fun getPools(): List<Pool>

}
