package io.github.andremion.slidingpuzzle.domain.puzzle

import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class PuzzleGame4X4Test {

    @Test
    fun `inversions count 18`() {
        val state = PuzzleState(
            tiles = listOf(
                2, 8, 3, 9,
                1, 6, 4, 10,
                7, 0, 5, 11,
                12, 13, 14, 15
            )
        )

        val count = state.inversionsCount()

        assertEquals(18, count)
    }

    @Test
    @Ignore("WIP")
    fun `easy`() = runTest {
        val startState = PuzzleState(
            tiles = listOf(
//                2, 8, 3, 9,
//                1, 6, 4, 10,
//                7, 0, 5, 11,
//                12, 13, 14, 15

//                6, 13, 7, 10,
//                8, 9, 11, 0,
//                15, 2, 12, 5,
//                14, 3, 1, 4,

                12, 1, 10, 2,
                7, 11, 4, 14,
                5, 0, 9, 15,
                8, 13, 6, 3

//                4, 1, 2, 3,
//                0, 5, 6, 7,
//                8, 9, 10, 11,
//                12, 13, 14, 15
            )
        )
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = PuzzleState(
            tiles = listOf(
                0, 1, 2, 3,
                4, 5, 6, 7,
                8, 9, 10, 11,
                12, 13, 14, 15
            )
        )
        assertEquals(expected, states.last())
    }
}
