package io.github.andremion.slidingpuzzle.presentation.game

import androidx.compose.runtime.Immutable

@Immutable
data class GameUiState(
    val moves: String = "0",
    val timer: String = "00:00:00",
    val isPaused: Boolean = false,
    val board: Board,
    val fab: Fab = Fab.None,
    val hint: Hint = Hint.None,
) {
    data class Board(
        val tiles: List<Tile>,
        val columns: Int,
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

    sealed interface Hint {
        data object None : Hint
        data class Goal(val board: Board) : Hint
        data object Solve : Hint
    }
}
