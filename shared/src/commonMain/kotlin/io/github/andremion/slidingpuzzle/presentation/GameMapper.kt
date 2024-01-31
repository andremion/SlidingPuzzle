package io.github.andremion.slidingpuzzle.presentation

import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleState

fun PuzzleState.transform(): List<GameUiState.Piece> =
    tiles.map { tile ->
        if (tile == 0) {
            GameUiState.Piece.Blank
        } else {
            GameUiState.Piece.Tile(tile)
        }
    }
