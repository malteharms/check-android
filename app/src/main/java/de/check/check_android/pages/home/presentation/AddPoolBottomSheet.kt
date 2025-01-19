package de.check.check_android.pages.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.check.check_android.pages.home.data.HomeState

@Composable
fun AddPoolBottomSheet(
    state: HomeState,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
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