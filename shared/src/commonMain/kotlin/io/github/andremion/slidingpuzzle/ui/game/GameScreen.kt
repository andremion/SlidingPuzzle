package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Lightbulb
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
            PuzzleStats(
                moves = uiState.stats.moves,
                timer = uiState.stats.timer,
                isPaused = uiState.stats.isPaused
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
    when (val dialog = uiState.dialog) {
        GameUiState.Dialog.None -> Unit

        is GameUiState.Dialog.Goal -> {
            GoalDialog(
                board = dialog.board,
                onDismiss = { onUiEvent(GameUiEvent.DismissDialogClick(dialog)) }
            )
        }

        is GameUiState.Dialog.Congratulations -> {
            CongratulationsDialog(
                moves = dialog.moves,
                time = dialog.time,
                board = dialog.board,
                onDismiss = { onUiEvent(GameUiEvent.DismissDialogClick(dialog)) }
            )
        }
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
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = "Hint"
                    )
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = "Goal") }
            ) {
                IconButton(
                    modifier = Modifier.tooltipAnchor(),
                    onClick = { onUiEvent(GameUiEvent.GoalClick) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Flag,
                        contentDescription = "Goal"
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

@Composable
private fun GoalDialog(
    board: GameUiState.Board,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "This is your goal")
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PuzzleBoard(
                    modifier = Modifier
                        .size(150.dp),
                    tiles = board.tiles.map(GameUiState.Board.Tile::number),
                    columns = board.columns,
                    tileTextStyle = MaterialTheme.typography.headlineSmall,
                    isEnabled = false,
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Dismiss")
            }
        },
    )
}

@Composable
private fun CongratulationsDialog(
    moves: String,
    time: String,
    board: GameUiState.Board,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "Congratulations!!! 🎉🥳")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PuzzleStats(
                    moves = moves,
                    timer = time,
                    isPaused = false
                )
                PuzzleBoard(
                    modifier = Modifier
                        .size(150.dp),
                    tiles = board.tiles.map(GameUiState.Board.Tile::number),
                    columns = board.columns,
                    tileTextStyle = MaterialTheme.typography.headlineSmall,
                    isEnabled = false,
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Dismiss")
            }
        },
    )
}
