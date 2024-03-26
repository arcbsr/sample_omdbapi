package com.rafiur.assessmentproject.utils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

enum class SortOption {
    NAME_ASCENDING,
    NAME_DESCENDING,
    DATE_ASCENDING,
    DATE_DESCENDING
}

@Composable
fun SortMenu(
    onSortSelected: (SortOption) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Sort by",
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onSortSelected(SortOption.NAME_ASCENDING)
            }) {
                Text("Name Ascending")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onSortSelected(SortOption.NAME_DESCENDING)
            }) {
                Text("Name Descending")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onSortSelected(SortOption.DATE_ASCENDING)
            }) {
                Text("Date Ascending")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onSortSelected(SortOption.DATE_DESCENDING)
            }) {
                Text("Date Descending")
            }
        }
    }
}