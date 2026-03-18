package com.example.game.features.memory_game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import com.example.game.features.memory_game.components.MemoryCardItem

@Composable
fun MemoryGameScreen(
    viewModel: MemoryGameViewModel = viewModel(),
    onGameFinished: () -> Unit
) {
    val cards by viewModel.cards.collectAsState()
    val moves by viewModel.moves.collectAsState()
    val playerName by viewModel.playerName.collectAsState()
    val isGameFinished by viewModel.isGameFinished.collectAsState()
    val elapsedSeconds by viewModel.elapsedSeconds.collectAsState()

    LaunchedEffect(isGameFinished) {
        if (isGameFinished) {
            onGameFinished()
        }
    }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeText = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Jugador: $playerName",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Movimientos: $moves",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Tiempo: $timeText",
            style = MaterialTheme.typography.bodyLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                MemoryCardItem(
                    card = card,
                    onClick = { viewModel.onCardClicked(index) }
                )
            }
        }
    }
}