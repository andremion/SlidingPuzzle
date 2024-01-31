package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.presentation.game.GameUiState

private val BoardInnerPadding = 8.dp
private val TileRoundedCornerSize = 8.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PuzzleBoard(
    modifier: Modifier,
    tiles: List<GameUiState.Tile>,
    columns: Int,
    onClick: (tile: GameUiState.Tile) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(BoardInnerPadding),
        horizontalArrangement = Arrangement.spacedBy(BoardInnerPadding),
        verticalArrangement = Arrangement.spacedBy(BoardInnerPadding),
    ) {
        tiles
            .chunked(columns)
            .forEach { row ->
                items(
                    items = row,
                    key = GameUiState.Tile::number
                ) { tile ->
                    BoardTile(
                        modifier = Modifier.animateItemPlacement(),
                        tile = tile,
                        onClick = { onClick(tile) }
                    )
                }
            }
    }
}

@Composable
private fun BoardTile(
    modifier: Modifier,
    tile: GameUiState.Tile,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        if (tile.number > 0) {
            Button(
                modifier = Modifier
                    .aspectRatio(1f),
                shape = RoundedCornerShape(TileRoundedCornerSize),
                onClick = onClick,
            ) {
                Text(
                    text = tile.number.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}
