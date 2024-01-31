package io.github.andremion.slidingpuzzle.presentation.game

sealed interface GameUiEvent {
    data class TileClick(val tile: GameUiState.Tile) : GameUiEvent
    data object PauseClick : GameUiEvent
    data object ResumeClick : GameUiEvent
    data object ReplayClick : GameUiEvent
    data object SolveClick : GameUiEvent
}
