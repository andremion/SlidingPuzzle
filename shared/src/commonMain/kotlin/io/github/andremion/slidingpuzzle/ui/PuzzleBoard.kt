package io.github.andremion.slidingpuzzle.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.presentation.GameUiEvent
import io.github.andremion.slidingpuzzle.presentation.GameUiState
import io.github.andremion.slidingpuzzle.presentation.GameViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun PuzzleBoard() {
    val viewModel = koinViewModel(GameViewModel::class)

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    BoardContent(
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun BoardContent(
    uiState: GameUiState,
    onUiEvent: (GameUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "8 Puzzle Game"
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onUiEvent(GameUiEvent.ShuffleClick) }
                    ) {
                        Text(
                            text = "Shuffle"
                        )
                    }
                    IconButton(
                        onClick = { onUiEvent(GameUiEvent.SolveClick) }
                    ) {
                        Text(
                            text = "Solve"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .height(300.dp)
                    .align(Alignment.Center),
                columns = GridCells.Fixed(uiState.columns),
                contentPadding = PaddingValues(16.dp),
            ) {
                uiState.pieces
                    .chunked(uiState.columns)
                    .forEach { row ->
                        items(
                            items = row,
                            key = GameUiState.Piece::number
                        ) { piece ->
                            Piece(
                                modifier = Modifier.animateItemPlacement(),
                                piece = piece
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun Piece(
    modifier: Modifier,
    piece: GameUiState.Piece
) {
    Box(
        modifier = modifier
    ) {
        when (piece) {
            is GameUiState.Piece.Blank -> {

            }

            is GameUiState.Piece.Tile -> {
                Button(
                    onClick = { piece.movePiece(1) }
                ) {
                    Text(
                        text = piece.number.toString()
                    )
                }
            }
        }
    }
}
