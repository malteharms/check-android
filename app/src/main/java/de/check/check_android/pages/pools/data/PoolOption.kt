package de.check.check_android.pages.pools.data

import androidx.compose.ui.graphics.vector.ImageVector


data class PoolOption(
    val name: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val danger: Boolean = false
)

enum class PoolOptionType {
    NORMAL,
    DANGER
}