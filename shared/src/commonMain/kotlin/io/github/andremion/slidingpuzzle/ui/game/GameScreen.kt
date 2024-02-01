package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.presentation.game.GameUiEvent
import io.github.andremion.slidingpuzzle.presentation.game.GameUiState
import io.github.andremion.slidingpuzzle.presentation.game.GameViewModel
import io.github.andremion.slidingpuzzle.ui.animation.FadeAnimatedVisibility
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

    LaunchedEffect(true) {
        viewModel.onUiEvent(GameUiEvent.ReplayClick)
    }
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
                title = { Text(text = "Sliding Puzzle Game") },
            )
        },
        bottomBar = { BottomBar(uiState.fab, onUiEvent) }
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
                timer = uiState.timer,
                isPaused = uiState.isPaused
            )
            PuzzleBoard(
                modifier = Modifier
                    .size(300.dp),
                tiles = uiState.board.tiles.map(GameUiState.Board.Tile::number),
                columns = uiState.board.columns,
                tileTextStyle = MaterialTheme.typography.headlineLarge,
                onClick = { tile -> onUiEvent(GameUiEvent.TileClick(tile)) }
            )
        }
    }
    when (val hint = uiState.hint) {
        GameUiState.Hint.None -> Unit

        is GameUiState.Hint.Goal -> {
            AlertDialog(
                title = {
                    Text(text = "This is your goal")
                },
                text = {
                    PuzzleBoard(
                        modifier = Modifier
                            .size(150.dp),
                        tiles = hint.board.tiles.map(GameUiState.Board.Tile::number),
                        columns = hint.board.columns,
                        tileTextStyle = MaterialTheme.typography.headlineSmall,
                        isEnabled = false,
                    )
                },
                onDismissRequest = { onUiEvent(GameUiEvent.DismissHintClick) },
                confirmButton = {
                    Button(
                        onClick = { onUiEvent(GameUiEvent.DismissHintClick) }
                    ) {
                        Text(text = "Dismiss")
                    }
                },
            )
        }

        GameUiState.Hint.Solve -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomBar(
    fab: GameUiState.Fab,
    onUiEvent: (GameUiEvent) -> Unit
) {
    BottomAppBar(
        actions = {
            PlainTooltipBox(
                tooltip = { Text(text = "Hint") }
            ) {
                IconButton(
                    modifier = Modifier.tooltipAnchor(),
                    onClick = { onUiEvent(GameUiEvent.HintClick) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lightbulb,
                        contentDescription = "Hint"
                    )
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = "Replay") }
            ) {
                IconButton(
                    modifier = Modifier.tooltipAnchor(),
                    onClick = { onUiEvent(GameUiEvent.ReplayClick) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Replay,
                        contentDescription = "Replay"
                    )
                }
            }
        },
        floatingActionButton = {
            PlainTooltipBox(
                tooltip = { Text(text = "Resume timer") }
            ) {
                FadeAnimatedVisibility(
                    isVisible = fab == GameUiState.Fab.Resume,
                ) {
                    FloatingActionButton(
                        modifier = Modifier.tooltipAnchor(),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = { onUiEvent(GameUiEvent.ResumeClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Resume timer"
                        )
                    }
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = "Pause timer") }
            ) {
                FadeAnimatedVisibility(
                    isVisible = fab == GameUiState.Fab.Pause,
                ) {
                    FloatingActionButton(
                        modifier = Modifier.tooltipAnchor(),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = { onUiEvent(GameUiEvent.PauseClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Pause,
                            contentDescription = "Pause timer"
                        )
                    }
                }
            }
        }
    )
}
