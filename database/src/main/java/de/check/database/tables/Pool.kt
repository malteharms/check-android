package de.check.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.check.core.getTimestampFromDate
import java.time.LocalDateTime


@Entity(tableName = "pools")
data class Pool(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val creator: Int,
    val created: Long = getTimestampFromDate(LocalDateTime.now()),
    val updated: Long
)
