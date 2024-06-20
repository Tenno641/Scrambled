package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

class GameViewModelTest {

    private val viewModel = GameViewModel()

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(currentGameUiState.gameScore, SCORE_AFTER_FIRST_CORRECT_ANSWER)
    }

    @Test
    fun gameViewModel_IncorrectWordGuessed_FlagErrorSet() {
        val incorrectPlayerGuess = "whatever"

        viewModel.updateUserGuess(incorrectPlayerGuess)
        viewModel.checkUserGuess()

        val currentGameState = viewModel.uiState.value

        assertTrue(currentGameState.isGuessedWordWrong)
        assertEquals(0, currentGameState.gameScore)

    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unScrambled = getUnscrambledWord(gameUiState.currentScrambledWord)

        assertNotEquals(gameUiState.currentScrambledWord, unScrambled)
        assertTrue(gameUiState.guessedWordCount == 1)
        assertFalse(gameUiState.isGuessedWordWrong)
        assertTrue(gameUiState.gameScore == 0)
        assertFalse(gameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameState.currentScrambledWord)
            assertEquals(expectedScore, currentGameState.gameScore)
        }
        assertEquals(MAX_NO_OF_WORDS, currentGameState.guessedWordCount)
        assertTrue(currentGameState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.guessedWordCount
        viewModel.skipWord()
        currentGameUiState = viewModel.uiState.value
        assertEquals(0, currentGameUiState.gameScore)
        assertEquals(lastWordCount + 1, currentGameUiState.guessedWordCount)
    }

}