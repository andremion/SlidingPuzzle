package io.github.andremion.slidingpuzzle.presentation.game

sealed interface GameUiEvent {
    data class TileClick(val tile: Int) : GameUiEvent
    data object PauseClick : GameUiEvent
    data object ResumeClick : GameUiEvent
    data object ReplayClick : GameUiEvent
    data object HintClick : GameUiEvent
    data object GoalClick : GameUiEvent
    data class DismissDialogClick(val dialog: GameUiState.Dialog) : GameUiEvent
    data object DismissSnackbar : GameUiEvent
}
