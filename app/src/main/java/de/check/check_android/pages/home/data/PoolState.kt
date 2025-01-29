package de.check.check_android.pages.home.data

import de.check.database.tables.Pool

data class PoolState(
    val isAddingNewPool: Boolean = false,
    val isOptionsSheetOpen: Boolean = false,

    val selectedPool: Pool? = null,

    val pools: List<Pool> = emptyList(),
    val newPoolTitle: String = ""
)
