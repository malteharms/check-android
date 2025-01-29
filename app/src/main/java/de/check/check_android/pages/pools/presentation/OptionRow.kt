package de.check.check_android.pages.pools.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.check.check_android.pages.pools.data.PoolOption


@Composable
fun OptionRow(
    options: List<PoolOption>,
    danger: Boolean = false
) {
    val tint = if (danger)  {
        MaterialTheme.colorScheme.error
    } else MaterialTheme.colorScheme.onBackground

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach {
            Column (
                modifier = Modifier.clickable { it.onClick() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(it.icon, contentDescription = null, tint = tint)
                Text(it.name, color = tint)
            }
        }
    }

}