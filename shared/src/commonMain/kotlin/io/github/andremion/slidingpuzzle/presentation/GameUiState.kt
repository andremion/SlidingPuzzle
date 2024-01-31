package io.github.andremion.slidingpuzzle.presentation

data class GameUiState(
    val pieces: List<Piece>,
    val columns: Int,
) {
    sealed interface Piece {
        val number: Int

        data object Blank : Piece {
            override val number: Int = 0
        }

        data class Tile(
            override val number: Int,
        ) : Piece {
            fun movePiece(direction: Int, animated: Boolean = true) {

            }
        }
    }
}
