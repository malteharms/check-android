package de.check.check_android.pages.pools.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.check.check_android.pages.pools.data.PoolOption
import de.check.check_android.pages.pools.data.PoolOptionType
import de.check.check_android.pages.pools.domain.PoolEvent
import de.check.database.tables.Pool


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsBottomSheet(
    pool: Pool,
    onDismissRequest: () -> Unit,
    onEvent: (PoolEvent) -> Unit
) {
    val options: Map<PoolOptionType, List<PoolOption>> = mapOf(
        PoolOptionType.NORMAL to listOf(),

        PoolOptionType.DANGER to listOf(
            PoolOption(
                name = "Delete",
                icon = Icons.Default.Delete,
                danger = true,
                onClick = { onEvent(PoolEvent.RemovePool) }
            )
        )
    )


    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            options[PoolOptionType.NORMAL]?.let {
                OptionRow(it)
            }

            options[PoolOptionType.DANGER]?.let {
                HorizontalDivider(modifier = Modifier.padding(bottom = 10.dp))
                OptionRow(it, danger = true)
            }

        }
    }
}
