package io.github.andremion.slidingpuzzle.domain.puzzle

import kotlin.math.sqrt

data class PuzzleState @Throws(IllegalArgumentException::class) constructor(
    val tiles: List<Int>
) {

    data class TilePosition(
        val row: Int,
        val column: Int
    )

    val matrixSize = sqrt(tiles.size.toDouble()).toInt()

    init {
        require(sqrt(tiles.size.toDouble()) % 1.0 == 0.0) {
            "The tiles must represent a square matrix"
        }
        val check = List(tiles.size) { it }
        require(tiles.sorted() == check) {
            "The tiles must contain only a range from zero to the size - 1"
        }
    }

    override fun toString(): String {
        val maxTileLength = tiles.max().toString().length
        return "\n" +
            tiles.chunked(matrixSize)
                .joinToString(separator = "\n") { row ->
                    row.joinToString(separator = "") { tile ->
                        "[${tile.toString().padStart(length = maxTileLength)}]"
                    }
                }
    }
}

/**
 * Get the position of the tile in the matrix.
 */
fun PuzzleState.getPosition(tile: Int): PuzzleState.TilePosition {
    val indexOf = tiles.indexOf(tile)
    indexOf != -1 || error("Tile #$tile not found")
    return PuzzleState.TilePosition(
        row = indexOf / matrixSize,
        column = indexOf % matrixSize
    )
}

/**
 * Get the next possible states (successors) which can be reached from the current state.
 */
fun PuzzleState.getSuccessors(): List<PuzzleState> {
    val states = mutableListOf<PuzzleState>()
    val blankPosition = getPosition(0)
    var newPosition: PuzzleState.TilePosition

    if (blankPosition.row > 0) { // can we move up?
        newPosition = blankPosition.copy(row = blankPosition.row - 1)
        states.add(permuted(blankPosition, newPosition))
    }
    if (blankPosition.row < matrixSize - 1) { // can we move down?
        newPosition = blankPosition.copy(row = blankPosition.row + 1)
        states.add(permuted(blankPosition, newPosition))
    }
    if (blankPosition.column > 0) { // can we move left?
        newPosition = blankPosition.copy(column = blankPosition.column - 1)
        states.add(permuted(blankPosition, newPosition))
    }
    if (blankPosition.column < matrixSize - 1) { // can we move right?
        newPosition = blankPosition.copy(column = blankPosition.column + 1)
        states.add(permuted(blankPosition, newPosition))
    }

    return states
}

fun PuzzleState.permuted(a: PuzzleState.TilePosition, b: PuzzleState.TilePosition): PuzzleState {
    val indexA = indexOf(a)
    val indexB = indexOf(b)
    val permuted = buildList {
        addAll(tiles)
        val tmp = this[indexB]
        this[indexB] = this[indexA]
        this[indexA] = tmp
    }
    return PuzzleState(permuted)
}

fun PuzzleState.indexOf(position: PuzzleState.TilePosition): Int =
    position.row * matrixSize + position.column
