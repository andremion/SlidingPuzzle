package io.github.andremion.slidingpuzzle.presentation.game

import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleState

fun PuzzleState.transform(): GameUiState.Board =
    GameUiState.Board(
        tiles = tiles.map(GameUiState.Board::Tile),
        columns = matrixSize,
    )
