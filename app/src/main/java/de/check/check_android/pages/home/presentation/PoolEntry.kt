package de.check.check_android.pages.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import de.check.database.tables.Pool


@Composable
fun PoolEntry(
    pool: Pool
) {
    // TODO receive PoolID and fetch pool from database

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val cardWidth = screenWidth * 0.4f
    val cardHeight = 100.dp

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .size(width = cardWidth, height = cardHeight)
    ) {
        Column (
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 15.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = pool.title, fontWeight = FontWeight.Bold)
                IconButton(onClick = {}, modifier = Modifier.size(18.dp)) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
            }

            Text(text = "Malte, Ina", fontSize = 13.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Balance()
        }
    }
}
