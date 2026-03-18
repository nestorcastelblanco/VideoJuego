package com.example.game.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.game.features.memory_game.MemoryGameScreen
import com.example.game.features.memory_game.MemoryGameViewModel
import com.example.game.features.memory_game.home.HomeScreen
import com.example.game.features.memory_game.result.ResultScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameViewModel: MemoryGameViewModel = viewModel()

    val playerName by gameViewModel.playerName.collectAsState()
    val moves by gameViewModel.moves.collectAsState()
    val elapsedSeconds by gameViewModel.elapsedSeconds.collectAsState()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                playerName = playerName,
                onNameChange = { gameViewModel.setPlayerName(it) },
                onStartGame = {
                    gameViewModel.startGame()
                    navController.navigate(Routes.GAME)
                }
            )
        }
        composable(Routes.GAME) {
            MemoryGameScreen(
                viewModel = gameViewModel,
                onGameFinished = {
                    navController.navigate(Routes.RESULT)
                }
            )
        }
        composable(Routes.RESULT) {
            ResultScreen(
                playerName = playerName,
                moves = moves,
                elapsedSeconds = elapsedSeconds,
                onPlayAgain = {
                    gameViewModel.startGame()
                    navController.navigate(Routes.GAME)
                },
                onBackHome = {
                    navController.navigate(Routes.HOME)
                }
            )
        }
    }
}