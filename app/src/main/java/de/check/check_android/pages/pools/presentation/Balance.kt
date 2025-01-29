package de.check.check_android.pages.pools.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Balance() {
    // TODO colors need to be fetched from database
    val colors = mapOf(
        "Malte" to Color.Blue,
        "Ina" to Color.Red
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Blue)
                .fillMaxHeight()
        )

        Box(
            modifier = Modifier
                .weight(2f)
                .background(Color.Red)
                .fillMaxHeight()
        )

    }
}
