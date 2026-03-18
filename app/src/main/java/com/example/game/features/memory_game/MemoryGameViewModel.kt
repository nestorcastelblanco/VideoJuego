package com.example.game.features.memory_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.data.MemoryCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemoryGameViewModel : ViewModel() {

    private val _cards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val cards: StateFlow<List<MemoryCard>> = _cards.asStateFlow()

    private val _moves = MutableStateFlow(0)
    val moves: StateFlow<Int> = _moves.asStateFlow()

    private val _playerName = MutableStateFlow("")
    val playerName: StateFlow<String> = _playerName.asStateFlow()

    private val _isGameFinished = MutableStateFlow(false)
    val isGameFinished: StateFlow<Boolean> = _isGameFinished.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private var selectedCards = mutableListOf<Int>()
    private var isChecking = false
    private var timerJob: Job? = null

    init {
        startGame()
    }

    fun setPlayerName(name: String) {
        _playerName.value = name
    }

    fun startGame() {
        val values = listOf("🍎", "🚗", "🐶", "⚽", "🎵", "📚", "🌟", "🎲")
        val shuffledCards = (values + values)
            .shuffled()
            .mapIndexed { index, value ->
                MemoryCard(id = index, value = value)
            }

        _cards.value = shuffledCards
        _moves.value = 0
        _isGameFinished.value = false
        _elapsedSeconds.value = 0
        selectedCards.clear()
        isChecking = false
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedSeconds.value += 1
            }
        }
    }

    fun onCardClicked(index: Int) {
        if (isChecking) return
        if (_cards.value[index].isFaceUp) return
        if (_cards.value[index].isMatched) return
        if (selectedCards.contains(index)) return

        val currentCards = _cards.value.toMutableList()
        currentCards[index] = currentCards[index].copy(isFaceUp = true)
        selectedCards.add(index)
        _cards.value = currentCards.toList()

        if (selectedCards.size == 2) {
            checkCards()
        }
    }

    private fun checkCards() {
        val firstIndex = selectedCards[0]
        val secondIndex = selectedCards[1]

        _moves.value += 1
        isChecking = true

        viewModelScope.launch {
            val firstValue = _cards.value[firstIndex].value
            val secondValue = _cards.value[secondIndex].value

            if (firstValue == secondValue) {
                // Pareja correcta
                val updated = _cards.value.toMutableList()
                updated[firstIndex] = updated[firstIndex].copy(isMatched = true, isFaceUp = true)
                updated[secondIndex] = updated[secondIndex].copy(isMatched = true, isFaceUp = true)
                _cards.value = updated.toList()
                selectedCards.clear()
                isChecking = false

                // Verificar si el juego terminó
                if (_cards.value.all { it.isMatched }) {
                    timerJob?.cancel()
                    _isGameFinished.value = true
                }
            } else {
                // Pareja incorrecta — esperar 1 segundo y ocultar
                delay(1000)
                val updated = _cards.value.toMutableList()
                updated[firstIndex] = updated[firstIndex].copy(isFaceUp = false)
                updated[secondIndex] = updated[secondIndex].copy(isFaceUp = false)
                _cards.value = updated.toList()
                selectedCards.clear()
                isChecking = false
            }
        }
    }
}