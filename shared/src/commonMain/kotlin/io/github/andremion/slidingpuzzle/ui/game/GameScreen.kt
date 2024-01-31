package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.presentation.game.GameUiEvent
import io.github.andremion.slidingpuzzle.presentation.game.GameUiState
import io.github.andremion.slidingpuzzle.presentation.game.GameViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun GameScreen() {
    val viewModel = koinViewModel(GameViewModel::class)

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ScreenContent(
        uiState = uiState,
        onUiEvent = viewModel::onUiEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: GameUiState,
    onUiEvent: (GameUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Sliding Puzzle Game"
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { onUiEvent(GameUiEvent.SolveClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lightbulb,
                            contentDescription = "Solve"
                        )
                    }
                },
                floatingActionButton = {
                    Crossfade(
                        targetState = uiState.fab,
                    ) { fab ->
                        when (fab) {
                            GameUiState.Fab.Resume ->
                                FloatingActionButton(
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                    onClick = { onUiEvent(GameUiEvent.ResumeClick) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = "Resume timer"
                                    )
                                }

                            GameUiState.Fab.Pause ->
                                FloatingActionButton(
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                    onClick = { onUiEvent(GameUiEvent.PauseClick) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Pause,
                                        contentDescription = "Pause timer"
                                    )
                                }

                            null -> {
                                // no-op
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
        ) {
            PuzzleDashboard(
                moves = uiState.moves,
                timer = uiState.timer
            )
            PuzzleBoard(
                modifier = Modifier
                    .size(300.dp),
                tiles = uiState.tiles,
                columns = uiState.columns,
                onClick = { tile -> onUiEvent(GameUiEvent.TileClick(tile)) }
            )
        }
    }
}
