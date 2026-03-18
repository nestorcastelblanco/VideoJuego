package com.example.game.features.memory_game
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.data.MemoryCard
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

    private var selectedCards = mutableListOf<Int>()
    private var isChecking = false

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
                MemoryCard(
                    id = index,
                    value = value
                )
            }

        _cards.value = shuffledCards
        _moves.value = 0
        selectedCards.clear()
        isChecking = false
    }

    fun onCardClicked(index: Int) {
        val currentCards = _cards.value.toMutableList()

        if (isChecking) return
        if (currentCards[index].isFaceUp) return
        if (currentCards[index].isMatched) return
        if (selectedCards.contains(index)) return

        currentCards[index] = currentCards[index].copy(isFaceUp = true)
        selectedCards.add(index)
        _cards.value = currentCards

        if (selectedCards.size == 2) {
            checkCards()
        }
    }

    private fun checkCards() {
        val currentCards = _cards.value.toMutableList()
        val firstIndex = selectedCards[0]
        val secondIndex = selectedCards[1]

        val firstCard = currentCards[firstIndex]
        val secondCard = currentCards[secondIndex]

        _moves.value += 1
        isChecking = true

        viewModelScope.launch {
            if (firstCard.value == secondCard.value) {
                currentCards[firstIndex] = currentCards[firstIndex].copy(isMatched = true)
                currentCards[secondIndex] = currentCards[secondIndex].copy(isMatched = true)
            } else {
                delay(1000)
                currentCards[firstIndex] = currentCards[firstIndex].copy(isFaceUp = false)
                currentCards[secondIndex] = currentCards[secondIndex].copy(isFaceUp = false)
            }

            _cards.value = currentCards
            selectedCards.clear()
            isChecking = false
        }
    }

    fun isGameFinished(): Boolean {
        return _cards.value.all { it.isMatched }
    }
}