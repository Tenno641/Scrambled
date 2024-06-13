package com.example.unscramble.ui

data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val gameScore: Int = 0,
    val guessedWordCount: Int = 1,
    val hintNeeded: Boolean = false,
    val isGameOver: Boolean = false
)
