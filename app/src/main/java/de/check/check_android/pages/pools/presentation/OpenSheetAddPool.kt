package de.check.check_android.pages.pools.presentation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import de.check.check_android.R

@Composable
fun OpenSheetAddPool(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        text = { Text(stringResource(R.string.create_pool)) },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) }
    )
}


@Preview
@Composable
fun AddPoolButtonPreview() {
    OpenSheetAddPool {}
}