package io.github.andremion.slidingpuzzle.domain.puzzle

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PuzzleStateTest {

    @Test
    fun `tiles should represent a 3x3 square matrix`() {
        PuzzleState(
            tiles = List(9) { it }
        )
    }

    @Test
    fun `tiles should represent a 4x4 square matrix`() {
        PuzzleState(
            tiles = List(16) { it }
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `tiles don't represent a square matrix`() {
        PuzzleState(
            tiles = List(3) { it }
        )
    }

    @Test
    fun `tiles should contain a range from zero to 8`() {
        PuzzleState(
            tiles = List(9) { it }
        )
    }

    @Test
    fun `tiles should contain a range from zero to 15`() {
        PuzzleState(
            tiles = List(16) { it }
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `tiles don't contain zero`() {
        PuzzleState(
            tiles = List(9) { it + 1 }
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `tiles contain random values`() {
        val random = Random.Default
        PuzzleState(
            tiles = List(9) { random.nextInt() }
        )
    }

    @Test
    fun `should compare states properly`() {
        val state1 = PuzzleState(
            tiles = listOf(
                2, 8, 3,
                1, 6, 4,
                7, 0, 5
            )
        )
        val state2 = PuzzleState(
            tiles = listOf(
                2, 8, 3,
                1, 6, 4,
                7, 0, 5
            )
        )
        val state3 = PuzzleState(
            tiles = listOf(
                1, 6, 4,
                7, 0, 5,
                2, 8, 3
            ),
        )

        assertEquals(state1, state2)
        assertEquals(state1.hashCode(), state2.hashCode())

        assertNotEquals(state1, state3)
        assertNotEquals(state2, state3)
        assertNotEquals(state1.hashCode(), state3.hashCode())
        assertNotEquals(state2.hashCode(), state3.hashCode())

        val list = listOf(state1)
        assertTrue(state2 in list)
        assertFalse(state3 in list)
    }

    @Test
    fun `should get positions`() {
        val state = PuzzleState(
            tiles = listOf(
                2, 8, 3,
                1, 6, 4,
                7, 0, 5
            )
        )

        var position = state.getPosition(tile = 2)
        assertEquals(PuzzleState.TilePosition(row = 0, column = 0), position)

        position = state.getPosition(tile = 8)
        assertEquals(PuzzleState.TilePosition(row = 0, column = 1), position)

        position = state.getPosition(tile = 3)
        assertEquals(PuzzleState.TilePosition(row = 0, column = 2), position)

        position = state.getPosition(tile = 1)
        assertEquals(PuzzleState.TilePosition(row = 1, column = 0), position)

        position = state.getPosition(tile = 6)
        assertEquals(PuzzleState.TilePosition(row = 1, column = 1), position)

        position = state.getPosition(tile = 4)
        assertEquals(PuzzleState.TilePosition(row = 1, column = 2), position)

        position = state.getPosition(tile = 7)
        assertEquals(PuzzleState.TilePosition(row = 2, column = 0), position)

        position = state.getPosition(tile = 0)
        assertEquals(PuzzleState.TilePosition(row = 2, column = 1), position)

        position = state.getPosition(tile = 5)
        assertEquals(PuzzleState.TilePosition(row = 2, column = 2), position)
    }

    @Test
    fun `should get next successors`() {
        val state = PuzzleState(
            tiles = listOf(
                2, 8, 3,
                1, 6, 4,
                7, 0, 5
            )
        )

        val successors = state.getSuccessors()
        assertEquals(3, successors.size)

        val successor1 = successors[0]
        assertEquals(
            PuzzleState(
                tiles = listOf(
                    2, 8, 3,
                    1, 0, 4,
                    7, 6, 5
                )
            ),
            successor1
        )

        val successor2 = successors[1]
        assertEquals(
            PuzzleState(
                tiles = listOf(
                    2, 8, 3,
                    1, 6, 4,
                    0, 7, 5
                )
            ),
            successor2
        )

        val successor3 = successors[2]
        assertEquals(
            PuzzleState(
                tiles = listOf(
                    2, 8, 3,
                    1, 6, 4,
                    7, 5, 0
                )
            ),
            successor3
        )
    }
}
