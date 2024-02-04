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

@file:OptIn(ExperimentalResourceApi::class)

package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.presentation.game.GameUiEvent
import io.github.andremion.slidingpuzzle.presentation.game.GameUiState
import io.github.andremion.slidingpuzzle.presentation.game.GameViewModel
import io.github.andremion.slidingpuzzle.ui.animation.FadeAnimatedVisibility
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import slidingpuzzle.shared.generated.resources.Res

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
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(Res.string.game_title)) },
            )
        },
        bottomBar = {
            Column {
                if (uiState.isBusy) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Spacer(
                        modifier = Modifier.size(4.dp) // The height of the LinearProgressIndicator
                    )
                }
                BottomBar(uiState.fab, onUiEvent)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                blinkTimer = uiState.stats.isPaused
            )
            PuzzleBoard(
                modifier = Modifier
                    .size(300.dp),
                tiles = uiState.board.tiles,
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
                stats = dialog.stats,
                board = dialog.board,
                onDismiss = { onUiEvent(GameUiEvent.DismissDialogClick(dialog)) }
            )
        }
    }
    LaunchedEffect(uiState.snackbar) {
        when (uiState.snackbar) {
            GameUiState.Snackbar.None -> Unit

            is GameUiState.Snackbar.MovesAwayFromGoal -> {
                snackbarHostState.showSnackbar(
                    message = "Your goal is ${uiState.snackbar.moves} moves away",
                )
                onUiEvent(GameUiEvent.DismissSnackbar)
            }
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
                tooltip = { Text(text = stringResource(Res.string.game_hint_button)) },
            ) {
                IconButton(
                    onClick = { onUiEvent(GameUiEvent.HintClick) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = stringResource(Res.string.game_hint_button)
                    )
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(Res.string.game_goal_button)) }
            ) {
                IconButton(
                    onClick = { onUiEvent(GameUiEvent.GoalClick) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Flag,
                        contentDescription = stringResource(Res.string.game_goal_button)
                    )
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(Res.string.game_replay_button)) }
            ) {
                IconButton(
                    onClick = { onUiEvent(GameUiEvent.ReplayClick) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Replay,
                        contentDescription = stringResource(Res.string.game_replay_button)
                    )
                }
            }
        },
        floatingActionButton = {
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(Res.string.game_resume_button)) }
            ) {
                FadeAnimatedVisibility(
                    isVisible = fab == GameUiState.Fab.Resume,
                ) {
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = { onUiEvent(GameUiEvent.ResumeClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = stringResource(Res.string.game_resume_button)
                        )
                    }
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(Res.string.game_pause_button)) }
            ) {
                FadeAnimatedVisibility(
                    isVisible = fab == GameUiState.Fab.Pause,
                ) {
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = { onUiEvent(GameUiEvent.PauseClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Pause,
                            contentDescription = stringResource(Res.string.game_pause_button)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlainTooltipBox(
    tooltip: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { tooltip() } },
        state = rememberTooltipState(),
        content = content
    )
}

@Composable
private fun GoalDialog(
    board: GameUiState.Board,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(Res.string.game_dialog_goal_title))
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PuzzleBoard(
                    modifier = Modifier
                        .size(150.dp),
                    tiles = board.tiles,
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
                Text(text = stringResource(Res.string.game_dialog_dismiss_button))
            }
        },
    )
}

@Composable
private fun CongratulationsDialog(
    stats: GameUiState.Stats,
    board: GameUiState.Board,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(Res.string.game_dialog_congratulations_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PuzzleStats(
                    moves = stats.moves,
                    timer = stats.timer,
                    blinkTimer = false
                )
                PuzzleBoard(
                    modifier = Modifier
                        .size(150.dp),
                    tiles = board.tiles,
                    columns = board.columns,
                    tileTextStyle = MaterialTheme.typography.headlineSmall,
                    isEnabled = board.isEnabled,
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = stringResource(Res.string.game_dialog_dismiss_button))
            }
        },
    )
}
