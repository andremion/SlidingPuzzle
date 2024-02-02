package io.github.andremion.slidingpuzzle.presentation.game

import androidx.compose.runtime.Immutable

data class GameUiState(
    val stats: Stats = Stats(),
    val board: Board = Board(),
    val fab: Fab = Fab.None,
    val dialog: Dialog = Dialog.None,
    val snackbar: Snackbar = Snackbar.None,
) {

    data class Stats(
        val moves: String = "0",
        val timer: String = "00:00:00",
        val isPaused: Boolean = false,
    )

    @Immutable
    data class Board(
        val tiles: List<Int> = List(9) { it },
        val columns: Int = 3,
        val isEnabled: Boolean = true,
    )

    sealed interface Fab {
        data object None : Fab
        data object Resume : Fab
        data object Pause : Fab
    }

    sealed interface Dialog {
        data object None : Dialog
        data class Goal(val board: Board) : Dialog
        data class Congratulations(
            val stats: Stats,
            val board: Board
        ) : Dialog
    }

    sealed interface Snackbar {
        data object None : Snackbar
        data class MovesAwayFromGoal(val moves: Int) : Snackbar
    }
}
