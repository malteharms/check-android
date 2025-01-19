package de.check.check_android.pages.home.data

import de.check.database.tables.Pool

data class HomeState(
    val isAddingNewPool: Boolean = false,
    val pools: List<Pool> = emptyList(),
    val newPoolTitle: String = ""
)
