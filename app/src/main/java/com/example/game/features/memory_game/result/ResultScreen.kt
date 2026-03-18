package com.example.game.features.memory_game.result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    playerName: String,
    moves: Int,
    elapsedSeconds: Int,
    onPlayAgain: () -> Unit,
    onBackHome: () -> Unit
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeText = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "¡Juego terminado!",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Jugador: $playerName",
                    style = MaterialTheme.typography.titleMedium)
                Text(text = "Movimientos: $moves")
                Text(text = "Tiempo: $timeText")
                Text(text = "¡Encontraste todas las parejas! 🎉")
            }
        }

        Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth()) {
            Text("Jugar de nuevo")
        }
        Button(onClick = onBackHome, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al inicio")
        }
    }
}