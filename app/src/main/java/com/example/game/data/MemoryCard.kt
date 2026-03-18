package com.example.game.data

data class MemoryCard(
    val id: Int,
    val value: String,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)