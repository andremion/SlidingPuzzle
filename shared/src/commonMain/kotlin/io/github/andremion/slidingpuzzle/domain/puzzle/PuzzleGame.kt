/*
 *    Copyright 2024. André Luiz Oliveira Rêgo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.andremion.slidingpuzzle.domain.puzzle

import io.github.andremion.slidingpuzzle.domain.search.AstarSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

class PuzzleGame(
    initialState: PuzzleState
) {
    var state: PuzzleState = initialState
        private set

    var moves: Int = 0
        private set

    val goal: PuzzleState = initialState.getSolvableState()
        ?: error("Puzzle is not solvable")

    val isSolved: Boolean
        get() = state == goal

    fun move(tile: Int) {
        val blankPosition = state.getPosition(0)
        val tilePosition = state.getPosition(tile)
        val newState = state.permuted(blankPosition, tilePosition)
        if (!state.getSuccessors().contains(newState)) {
            error("Tile #$tile cannot be moved")
        }
        state = newState
        moves++
    }

    fun replace(newState: PuzzleState) {
        state = newState
        moves++
    }

    suspend fun solve(): List<PuzzleState> =
        withContext(Dispatchers.Default) {
            println("goal: $goal")

            val states = AstarSearch(
                heuristics = { data -> data.heuristic(goal) },
                successors = PuzzleState::getSuccessors
            ).performSearch(
                start = state,
                goal = goal,
            )

            states
        }
}

/**
 * ```
 * [0][1][2]
 * [3][4][5]
 * [6][7][8]
 * ```
 * ```
 * [ 0][ 1][ 2][ 3]
 * [ 4][ 5][ 6][ 7]
 * [ 8][ 9][10][11]
 * [12][13][14][15]
 * ```
 */
private val SequentialGoalStates = mapOf(
    3 to PuzzleState(
        tiles = listOf(
            0, 1, 2,
            3, 4, 5,
            6, 7, 8
        )
    ),
    4 to PuzzleState(
        tiles = listOf(
            0, 1, 2, 3,
            4, 5, 6, 7,
            8, 9, 10, 11,
            12, 13, 14, 15
        )
    ),
)

/**
 * ```
 * [1][2][3]
 * [8][0][4]
 * [7][6][5]
 * ```
 * ```
 * [ 1][ 2][ 3][ 4]
 * [12][13][14][ 5]
 * [11][ 0][15][ 6]
 * [10][ 9][ 8][ 7]
 * ```
 */
private val SpiralGoalStates = mapOf(
    3 to PuzzleState(
        tiles = listOf(
            1, 2, 3,
            8, 0, 4,
            7, 6, 5
        )
    ),
    4 to PuzzleState(
        tiles = listOf(
            1, 2, 3, 4,
            12, 13, 14, 5,
            11, 0, 15, 6,
            10, 9, 8, 7
        )
    ),
)

fun PuzzleState.getSolvableState(): PuzzleState? {
    val sequentialGoal = SequentialGoalStates.getValue(matrixSize)
    val spiralGoal = SpiralGoalStates.getValue(matrixSize)
    return listOf(sequentialGoal, spiralGoal)
        .firstOrNull(::isSolvable)
}

/**
 * Count the number of inversions of a [PuzzleState].
 *
 * An inversion is when a bigger number comes before a smaller number.
 *
 * Counting all the inversions means counting all the tiles which come after the base tile and are smaller than it,
 * repeating the counting for each tile as base and returning the sum of all counts.
 *
 * For example:
 * ```
 * [2][8][3]
 * [1][6][4]
 * [7][0][5]
 * ```
 * - The tile 2 has 1 inversion: [1].
 * - The tile 8 has 6 inversions: [3, 1, 6, 4, 7, 5].
 * - The tile 3 has 1 inversion: [1].
 * - The tile 6 has 2 inversions: [4, 5].
 * - The tile 7 has 1 inversion: [5].
 * - The tile 0 has 0 inversion.
 * - The tile 5 has 0 inversion.
 *
 * - The result is 1 + 6 + 1 + 2 + 1 = 11.
 */
fun PuzzleState.inversionsCount(): Int {
    var count = 0

    for (i in tiles.indices) {
        val base = tiles[i]
        if (base > 1) {
            for (j in i + 1 until tiles.size) {
                if (tiles[j] in 1..<base) {
                    count++
                }
            }
        }
    }

    return count
}

fun PuzzleState.isSolvable(goal: PuzzleState): Boolean {
    val startInversionsCount = inversionsCount()
    println("startInversionsCount: $startInversionsCount")
    val goalInversionsCount = goal.inversionsCount()
    println("goalInversionsCount: $goalInversionsCount")

    return if (matrixSize % 2 == 0) {
        val startBlankIndex = tiles.indexOf(0) / matrixSize
        val goalBlankIndex = goal.tiles.indexOf(0) / matrixSize
        goalInversionsCount % 2 == (startInversionsCount + goalBlankIndex + startBlankIndex) % 2
    } else {
        goalInversionsCount % 2 == startInversionsCount % 2
    }
}

/**
 * The heuristic is the sum of the Manhattan distances between the state and the goal state.
 */
fun PuzzleState.heuristic(goalState: PuzzleState): Int =
    tiles.fold(initial = 0) { distance, tile ->
        val position = getPosition(tile)
        val goalPosition = goalState.getPosition(tile)
        distance + manhattanDistance(position, goalPosition)
    }

/**
 * The Manhattan distance is the sum of the absolute values of the differences of the coordinates.
 */
fun manhattanDistance(
    position1: PuzzleState.TilePosition,
    position2: PuzzleState.TilePosition
): Int =
    abs(position1.row - position2.row) + abs(position1.column - position2.column)
