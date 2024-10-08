package de.malteharms.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.database.Notificationable
import de.malteharms.utils.model.DateExt


@Entity(tableName = "reminder_items")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val category: ReminderCategory = ReminderCategory.GENERAL,

    val todoRelation: Long? = null,
    val birthdayRelation: Long? = null,

    val dueDate: DateExt,
    val creationDate: DateExt = DateExt.now(),
    val lastUpdate: DateExt = DateExt.now()
): Notificationable

enum class ReminderCategory {
    GENERAL,
    BIRTHDAY,
    IMPORTANT_APPOINTMENT
}