package io.github.andremion.slidingpuzzle.presentation.game

import androidx.compose.runtime.Immutable

@Immutable
data class GameUiState(
    val moves: String = "0",
    val timer: String = "00:00:00",
    val isPaused: Boolean = false,
    val board: Board = Board(),
    val fab: Fab = Fab.None,
    val dialog: Dialog = Dialog.None,
) {
    data class Board(
        val tiles: List<Tile> = List(9, ::Tile),
        val columns: Int = 3,
        val isEnabled: Boolean = true,
    ) {
        data class Tile(
            val number: Int,
        )
    }

    sealed interface Fab {
        data object None : Fab
        data object Resume : Fab
        data object Pause : Fab
    }

    sealed interface Dialog {
        data object None : Dialog
        data class Goal(val board: Board) : Dialog
    }
}
