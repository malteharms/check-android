package de.check.check_android.pages.pools.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.check.check_android.DefaultAppBar
import de.check.check_android.pages.pools.data.PoolState
import de.check.check_android.pages.pools.domain.PoolEvent
import de.check.database.tables.Pool


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: PoolState,
    onEvent: (PoolEvent) -> Unit
) {
    Scaffold (
        floatingActionButton = { OpenSheetAddPool { onEvent(PoolEvent.OpenSheetAddPool) } },
        topBar = { DefaultAppBar() }
    ){ innerPadding ->
        FlowRow(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.pools.forEach { pool: Pool ->
                PoolEntry(
                    pool = pool,
                    onClick = { /* TODO navigate to details page */},
                    onOptionsOpen = { onEvent(PoolEvent.OpenSheetOptions(pool)) }
                )
            }
        }

        if (state.isAddingNewPool) {
            AddPoolBottomSheet(
                state = state,
                onDismissRequest = { onEvent(PoolEvent.CloseSheetAddPool) },
                onValueChange = { title -> onEvent(PoolEvent.SetNewPoolTitle(title)) },
                onSubmit = { onEvent(PoolEvent.AddNewPool) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (state.isOptionsSheetOpen) {
            state.selectedPool?.let {
                OptionsBottomSheet(
                    pool = it,
                    onDismissRequest = { onEvent(PoolEvent.CloseSheetOptions) },
                    onEvent = onEvent
                )
            }
        }
    }
}
