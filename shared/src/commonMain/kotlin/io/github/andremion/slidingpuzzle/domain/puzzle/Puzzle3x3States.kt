package io.github.andremion.slidingpuzzle.domain.puzzle

object Puzzle3x3States {

    val Shuffled = PuzzleState(
        tiles = List(9) { it }
    ).shuffled()

    val Easy = PuzzleState(
        tiles = listOf(
            1, 3, 4,
            8, 6, 2,
            7, 0, 5
        )
    )

    val Medium = PuzzleState(
        tiles = listOf(
            2, 8, 1,
            0, 4, 3,
            7, 6, 5
        )
    )

    val Hard = PuzzleState(
        tiles = listOf(
            2, 8, 1,
            4, 6, 3,
            0, 7, 5
        )
    )

    val Hardest = PuzzleState(
        tiles = listOf(
            5, 6, 7,
            4, 0, 8,
            3, 2, 1
        )
    )
}
