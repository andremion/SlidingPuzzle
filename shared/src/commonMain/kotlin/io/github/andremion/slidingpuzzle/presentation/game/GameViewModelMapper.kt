package io.github.andremion.slidingpuzzle.presentation.game

import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleState

fun PuzzleState.transform(): List<GameUiState.Tile> =
    tiles.map(GameUiState::Tile)
