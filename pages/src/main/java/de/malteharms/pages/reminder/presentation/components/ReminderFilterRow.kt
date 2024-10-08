package de.malteharms.pages.reminder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.malteharms.database.tables.ReminderCategory
import de.malteharms.pages.reminder.domain.ReminderEvent
import de.malteharms.pages.reminder.presentation.getCategoryRepresentation

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReminderFilterRow(
    filterList: List<de.malteharms.database.tables.ReminderCategory>,
    onEvent: (ReminderEvent) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        de.malteharms.database.tables.ReminderCategory.entries.forEach { category ->
            val isSelected: Boolean = filterList.contains(category)

            FilterChip(
                onClick = { onEvent(ReminderEvent.AddOrRemoveFilterCategory(category)) },
                label = { Text(getCategoryRepresentation(category)) },
                selected = isSelected,
            )
        }
    }
}