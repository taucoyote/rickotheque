package com.example.rickotheque.features.choice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Lets the user pick which transport every feature's repository should use
 * for the rest of the session: REST over Ktor, or GraphQL over Apollo.
 */
@Composable
fun ChoiceScreen(
    onRestSelected: () -> Unit,
    onGraphqlSelected: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text(text = "Choisissez la source de données")

        Button(onClick = onRestSelected) {
            Text(text = "REST (Ktor)")
        }

        Button(onClick = onGraphqlSelected) {
            Text(text = "GraphQL (Apollo)")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChoiceScreenPreview() {
    ChoiceScreen(onRestSelected = {}, onGraphqlSelected = {})
}
