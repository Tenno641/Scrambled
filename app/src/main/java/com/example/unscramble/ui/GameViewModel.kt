package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.SCORE_INCREASE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.gameScore.plus(SCORE_INCREASE)
            updateGameScore(updatedScore)
        } else {
            _uiState.update { it.copy(isGuessedWordWrong = true) }
        }
        updateUserGuess("")
    }

    private fun updateGameScore(updatedScore: Int) {
        _uiState.update {
            it.copy(
                hintNeeded = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                isGuessedWordWrong = false,
                guessedWordCount = it.guessedWordCount.inc(),
                gameScore = updatedScore,
            )
        }
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun skipWord() {
        _uiState.update {
            it.copy(
                currentScrambledWord = pickRandomWordAndShuffle(),
                guessedWordCount = it.guessedWordCount.inc(),
                isGuessedWordWrong = false,
                hintNeeded = false
            )
        }
        updateUserGuess("")
    }

    fun showAnswer() {
        _uiState.update { it.copy(hintNeeded = true) }
    }

}


