package de.malteharms.pages.reminder.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.malteharms.database.tables.ReminderCategory
import de.malteharms.database.tables.ReminderItem
import de.malteharms.pages.R
import de.malteharms.pages.reminder.data.ReminderState
import de.malteharms.pages.reminder.domain.ReminderEvent
import de.malteharms.pages.reminder.presentation.components.bottomsheet.CategoryChoice
import de.malteharms.pages.reminder.presentation.components.bottomsheet.EditableNotificationRow
import de.malteharms.pages.reminder.presentation.components.bottomsheet.EditableTitleRow
import de.malteharms.utils.model.DateExt
import java.time.LocalDate


@Composable
fun ReminderBottomSheet(
    item: ReminderItem?,
    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit
) {
    val dateDialogState = rememberMaterialDialogState()

    var title: TextFieldValue by remember {
        mutableStateOf(TextFieldValue(text = item?.title ?: ""))
    }

    var pickedDate: DateExt by remember {
        mutableStateOf(item?.dueDate ?: DateExt.now())
    }

    val formattedDate: String by remember {
        derivedStateOf { pickedDate.toString() }
    }

    val editableRowModifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
    val editableRowAlignment = Alignment.CenterVertically
    val editableRowArrangement = Arrangement.spacedBy(20.dp)

    val editable: Boolean = item?.birthdayRelation == null

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
    ) {
        
        // title row
        EditableTitleRow(
            title = title.text,
            editable = editable,
            onValueChange = { newText ->
                title = TextFieldValue(newText)
                onEvent(ReminderEvent.SetTitle(newText))
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        // category row
        CategoryChoice(
            item = item,
            onEvent = onEvent,
            editable = editable
        )


        // date picker row
        Row(
            modifier = editableRowModifier.clickable {
                if (editable) {
                    dateDialogState.show()
                }
            },
            verticalAlignment = editableRowAlignment,
            horizontalArrangement = editableRowArrangement
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            Text(
                text = formattedDate,
                color = if (editable) { MaterialTheme.colorScheme.onBackground } else Color.Gray
            )
        }

        EditableNotificationRow(
            modifier = editableRowModifier,
            arrangement = editableRowArrangement,
            state = state,
            onEvent = onEvent
        )

        // bottom row to display event buttons
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            if (item == null) {
                Button(
                    onClick = { onEvent(ReminderEvent.SaveItem) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text(
                    text = stringResource(R.string.save_reminder),
                    color = MaterialTheme.colorScheme.onSecondary
                ) }
            } else {
                Button(
                    onClick = { onEvent(ReminderEvent.RemoveItem(item)) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text(
                    text = stringResource(R.string.remove_reminder),
                    color = MaterialTheme.colorScheme.onError
                ) }
            }
        }

        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = stringResource(R.string.okay))
                negativeButton(text = stringResource(R.string.cancel))
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = stringResource(R.string.choose_a_date),
            ) { newDate: LocalDate ->
                pickedDate = DateExt(newDate.atStartOfDay())
                onEvent(ReminderEvent.SetDueDate(pickedDate))
            }
        }
    }
}


@Preview
@Composable
fun ReminderBottomSheetPreview() {

    val sampleItem = ReminderItem(
        id = -1,
        title = "Perso",
        category = ReminderCategory.GENERAL,
        todoRelation = null,
        dueDate = DateExt.now(),
        creationDate = DateExt.now(),
        lastUpdate = DateExt.now()
    )

    ReminderBottomSheet(
        item = sampleItem,
        state = ReminderState(),
        onEvent = {  }
    )
}
