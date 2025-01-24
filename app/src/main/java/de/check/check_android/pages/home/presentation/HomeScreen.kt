package de.check.check_android.pages.home.presentation

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
import de.check.check_android.pages.home.data.HomeState
import de.check.check_android.pages.home.domain.HomeEvent
import de.check.database.tables.Pool


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    Scaffold (
        floatingActionButton = { OpenSheetAddPool { onEvent(HomeEvent.OpenSheetAddPool) } },
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
                PoolEntry(pool)
            }
        }

        if (state.isAddingNewPool) {
            AddPoolBottomSheet(
                state = state,
                onDismissRequest = { onEvent(HomeEvent.CloseSheetAddPool) },
                onValueChange = { title -> onEvent(HomeEvent.SetNewPoolTitle(title)) },
                onSubmit = { onEvent(HomeEvent.AddNewPool) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
