package io.github.andremion.slidingpuzzle.domain.puzzle

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PuzzleGame3X3Test {

    @Test
    fun `inversions count 11`() {
        val state = PuzzleState(
            tiles = listOf(
                2, 8, 3,
                1, 6, 4,
                7, 0, 5
            )
        )

        val count = state.inversionsCount()

        assertEquals(11, count)
    }

    @Test
    fun `easy 1`() = runTest {
        val startState = Puzzle3x3States.Easy
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = listOf(
            PuzzleState(
                tiles = listOf(
                    1, 3, 4,
                    8, 6, 2,
                    7, 0, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 3, 4,
                    8, 0, 2,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 3, 4,
                    8, 2, 0,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 3, 0,
                    8, 2, 4,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 0, 3,
                    8, 2, 4,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 2, 3,
                    8, 0, 4,
                    7, 6, 5
                )
            ),
        )
        assertEquals(expected, states)
    }

    @Test
    fun `easy 2`() = runTest {
        val startState = PuzzleState(
            tiles = listOf(
                2, 8, 3,
                1, 6, 4,
                7, 0, 5
            )
        )
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = listOf(
            PuzzleState(
                tiles = listOf(
                    2, 8, 3,
                    1, 6, 4,
                    7, 0, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    2, 8, 3,
                    1, 0, 4,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    2, 0, 3,
                    1, 8, 4,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    0, 2, 3,
                    1, 8, 4,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 2, 3,
                    0, 8, 4,
                    7, 6, 5
                )
            ),
            PuzzleState(
                tiles = listOf(
                    1, 2, 3,
                    8, 0, 4,
                    7, 6, 5
                )
            ),
        )
        assertEquals(expected, states)
    }

    @Test
    fun `easy 3`() = runTest {
        val startState = PuzzleState(
            tiles = listOf(
                1, 2, 5,
                3, 0, 4,
                6, 7, 8
            )
        )
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = PuzzleState(
            tiles = listOf(
                0, 1, 2,
                3, 4, 5,
                6, 7, 8
            )
        )
        assertEquals(expected, states.last())
    }

    @Test
    fun `medium 1`() = runTest {
        val startState = Puzzle3x3States.Medium
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = PuzzleState(
            tiles = listOf(
                1, 2, 3,
                8, 0, 4,
                7, 6, 5
            )
        )
        assertEquals(expected, states.last())
    }

    @Test
    fun `medium 2`() = runTest {
        val startState = PuzzleState(
            tiles = listOf(
                1, 4, 2,
                6, 5, 8,
                7, 3, 0
            )
        )
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = PuzzleState(
            tiles = listOf(
                0, 1, 2,
                3, 4, 5,
                6, 7, 8
            )
        )
        assertEquals(expected, states.last())
    }

    @Test
    fun `hard`() = runTest {
        val startState = Puzzle3x3States.Hard
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = PuzzleState(
            tiles = listOf(
                1, 2, 3,
                8, 0, 4,
                7, 6, 5
            )
        )
        assertEquals(expected, states.last())
    }

    @Test
    fun `hardest`() = runTest {
        val startState = Puzzle3x3States.Hardest
        val game = PuzzleGame(startState)

        val states = game.solve()

        val expected = PuzzleState(
            tiles = listOf(
                1, 2, 3,
                8, 0, 4,
                7, 6, 5
            )
        )
        assertEquals(expected, states.last())
    }
}
