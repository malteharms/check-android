package de.malteharms.pages.reminder.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.database.tables.ReminderItem
import de.malteharms.pages.R
import de.malteharms.pages.components.data.Screens
import de.malteharms.pages.components.data.getBottomNavigationItems
import de.malteharms.pages.components.presentation.FloatingBottomNavigation
import de.malteharms.pages.components.presentation.TopBar
import de.malteharms.pages.reminder.data.ReminderState
import de.malteharms.pages.reminder.domain.ReminderEvent
import de.malteharms.pages.reminder.presentation.components.ReminderBottomSheet
import de.malteharms.pages.reminder.presentation.components.ReminderItemRow
import de.malteharms.pages.reminder.presentation.components.bottomsheet.AddButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderPage(
    navController: NavController,
    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit,
    hasNotifications: (Long) -> Boolean,
) {
    // string resources
    val strReminderTitle = stringResource(R.string.reminder_title)
    val strAddReminder = stringResource(R.string.add_reminder)
    val strShowAllReminder = stringResource(R.string.show_all_reminder)

    // states
    val sheetState = rememberModalBottomSheetState()
    var currentEditItem: ReminderItem? by remember {
        mutableStateOf(null)
    }

    Scaffold (
        topBar = { TopBar(navController, strReminderTitle) },
        bottomBar = {
            FloatingBottomNavigation(
                navController,
                getBottomNavigationItems(),
                currentItem = strReminderTitle
            )
        },
        floatingActionButton = {
            AddButton(
                text = strAddReminder,
                onClick = { onEvent(ReminderEvent.ShowNewDialog) }
            )
        }
    ){ paddingValues ->
        LazyColumn (
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(state.items) { reminderItem ->
                ReminderItemRow(
                    item = reminderItem,
                    hasNotifications = hasNotifications(reminderItem.id),
                    onClick =  {
                        currentEditItem = reminderItem
                        onEvent(ReminderEvent.ShowEditDialog(reminderItem))
                    }
                )
            }
            
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { navController.navigate(Screens.ReminderDetailsRoute.route) }
                    ) {
                        Text(text = strShowAllReminder, color = Color.Gray)
                    }
                }
            }
        }
    }

    if (state.isEditingItem) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),

            onDismissRequest = {
                onEvent(ReminderEvent.UpdateItem(currentEditItem!!))
                currentEditItem = null
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            ReminderBottomSheet(
                item = currentEditItem,
                state = state,
                onEvent = onEvent
            )
        }
    }

    if (state.isAddingItem) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),

            onDismissRequest = {
                onEvent(ReminderEvent.HideDialog)
                currentEditItem = null
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            ReminderBottomSheet(
                item = null,
                state = state,
                onEvent = onEvent
            )
        }
    }
}
