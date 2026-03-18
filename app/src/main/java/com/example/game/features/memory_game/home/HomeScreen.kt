package com.example.game.features.memory_game.home
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    playerName: String,
    onNameChange: (String) -> Unit,
    onStartGame: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Juego de Memoria",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Encuentra las 8 parejas ocultas en el tablero. " +
                            "Solo puedes voltear dos cartas por turno. " +
                            "Si fallas, las cartas se ocultarán nuevamente después de 1 segundo."
                )
            }
        }

        OutlinedTextField(
            value = playerName,
            onValueChange = onNameChange,
            label = { Text("Escribe tu nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = onStartGame,
            enabled = playerName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar juego")
        }
    }
}