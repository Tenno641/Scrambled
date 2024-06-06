package com.example.unscramble.ui

data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val gameScore: Int = 0,
    val guessedWordCount: Int = 0,
    val hintNeeded: Boolean = false
)
