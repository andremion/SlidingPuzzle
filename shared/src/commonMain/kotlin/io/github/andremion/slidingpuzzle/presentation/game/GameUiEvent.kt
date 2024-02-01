package io.github.andremion.slidingpuzzle.presentation.game

sealed interface GameUiEvent {
    data class TileClick(val tile: Int) : GameUiEvent
    data object PauseClick : GameUiEvent
    data object ResumeClick : GameUiEvent
    data object ReplayClick : GameUiEvent
    data object HintClick : GameUiEvent
    data object GoalClick : GameUiEvent
    data object DismissDialogClick : GameUiEvent
    data object SolveClick : GameUiEvent
}
