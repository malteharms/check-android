package de.check.check_android.pages.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import de.check.check_android.pages.home.data.HomeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPoolBottomSheet(
    modifier: Modifier,
    state: HomeState,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val sheetHeight = screenHeight * 0.4f
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.newPoolTitle,
                onValueChange = { onValueChange(it) },
                label = { Text("Pool name") }
            )

            ElevatedButton(
                onClick = { onSubmit() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Submit")
            }
        }
    }
}
