package io.github.andremion.slidingpuzzle.presentation.game

sealed interface GameUiEvent {
    data class TileClick(val tile: GameUiState.Tile) : GameUiEvent
    data object NewGameClick : GameUiEvent
    data object SolveClick : GameUiEvent
    data object ResumeClick : GameUiEvent
    data object PauseClick : GameUiEvent
}
