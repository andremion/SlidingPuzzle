package io.github.andremion.slidingpuzzle.presentation.game

sealed interface GameUiEffect {
    data class ShowHint(
        val board: GameUiState.Board,
    ) : GameUiEffect
}
