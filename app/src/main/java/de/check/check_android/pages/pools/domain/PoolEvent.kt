package de.check.check_android.pages.pools.domain

import de.check.database.tables.Pool

sealed interface PoolEvent {

    data object OpenSheetAddPool: PoolEvent
    data object CloseSheetAddPool: PoolEvent

    data class SetNewPoolTitle(val title: String): PoolEvent

    data object AddNewPool: PoolEvent
    data class OpenPool(val pool: Pool): PoolEvent
    data object RemovePool: PoolEvent

    data class OpenSheetOptions(val pool: Pool): PoolEvent
    data object CloseSheetOptions: PoolEvent
}