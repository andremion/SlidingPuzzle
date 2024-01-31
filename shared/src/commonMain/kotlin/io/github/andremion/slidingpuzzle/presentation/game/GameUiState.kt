package io.github.andremion.slidingpuzzle.presentation.game

data class GameUiState(
    val moves: String = "0",
    val timer: String = "00:00:00",
    val tiles: List<Tile>,
    val columns: Int,
    val fab: Fab? = null,
) {
    data class Tile(
        val number: Int,
    )

    sealed interface Fab {
        data object Resume : Fab
        data object Pause : Fab
    }
}
