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

package io.github.andremion.slidingpuzzle.presentation.game

import io.github.andremion.slidingpuzzle.domain.puzzle.Puzzle3x3States
import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleGame
import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleState
import io.github.andremion.slidingpuzzle.domain.time.Timer
import io.github.andremion.slidingpuzzle.domain.time.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.math.min
import kotlin.time.Duration

private const val HINT_MOVEMENTS_COUNT = 5
private const val HINT_MOVEMENTS_DELAY = 300L

class GameViewModel : ViewModel() {

    private var puzzleGame = getSolvableGame()
    private val timer = Timer(coroutineScope = viewModelScope)

    private val mutableState = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = mutableState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = GameUiState()
        )

    init {
        mutableState.update { uiState ->
            uiState.copy(
                board = uiState.board.copy(tiles = puzzleGame.state.tiles),
            )
        }
    }

    fun onUiEvent(event: GameUiEvent) {
        when (event) {
            is GameUiEvent.TileClick -> {
                runCatching { puzzleGame.move(tile = event.tile) }
                    .onSuccess { onTileMove() }
                    .onFailure {/* Tile cannot be moved */ }
            }

            GameUiEvent.PauseClick -> {
                pause()
            }

            GameUiEvent.ResumeClick -> {
                timer.resume(::onTimerTick)
            }

            GameUiEvent.ReplayClick -> {
                newGame()
            }

            GameUiEvent.HintClick -> {
                mutableState.update { uiState ->
                    uiState.copy(isBusy = true)
                }
                viewModelScope.launch {
                    // solved states also contain the current state (first item)
                    // and the goal state (last item).
                    // So we drop the first one and keep the remaining ones
                    // that includes the goal state.
                    val states = puzzleGame.solve().drop(1)
                    if (states.isNotEmpty()) {
                        moveSolvedStates(states)
                        mutableState.update { uiState ->
                            uiState.copy(isBusy = false)
                        }
                    }
                    onTileMove()
                }
            }

            GameUiEvent.GoalClick -> {
                mutableState.update { uiState ->
                    uiState.copy(
                        dialog = GameUiState.Dialog.Goal(
                            board = GameUiState.Board(
                                tiles = puzzleGame.goal.tiles,
                                columns = puzzleGame.goal.matrixSize,
                                isEnabled = false,
                            )
                        )
                    )
                }
            }

            is GameUiEvent.DismissDialogClick -> {
                if (event.dialog is GameUiState.Dialog.Congratulations) {
                    newGame()
                }
                mutableState.update { uiState ->
                    uiState.copy(dialog = GameUiState.Dialog.None)
                }
            }

            GameUiEvent.DismissSnackbar -> {
                mutableState.update { uiState ->
                    uiState.copy(snackbar = GameUiState.Snackbar.None)
                }
            }
        }
    }

    private fun onTileMove() {
        mutableState.update { uiState ->
            uiState.copy(
                stats = uiState.stats.copy(moves = puzzleGame.moves.toString()),
                board = uiState.board.copy(tiles = puzzleGame.state.tiles),
            )
        }
        if (puzzleGame.isSolved) {
            pause()
            mutableState.update { uiState ->
                uiState.copy(
                    dialog = GameUiState.Dialog.Congratulations(
                        stats = uiState.stats,
                        board = uiState.board.copy(
                            isEnabled = false,
                        ),
                    ),
                    fab = GameUiState.Fab.None,
                    snackbar = GameUiState.Snackbar.None,
                )
            }
        } else {
            timer.start(::onTimerTick)
        }
    }

    private suspend fun moveSolvedStates(states: List<PuzzleState>) {
        val hintMovementsCount = min(HINT_MOVEMENTS_COUNT, states.size)
        for (i in 0 until hintMovementsCount) {
            val state = states[i]
            puzzleGame.replace(newState = state)
            mutableState.update { uiState ->
                uiState.copy(
                    stats = uiState.stats.copy(moves = puzzleGame.moves.toString()),
                    board = uiState.board.copy(tiles = puzzleGame.state.tiles),
                )
            }
            delay(HINT_MOVEMENTS_DELAY)
        }
        val movesAwayFromGoal = states.size - hintMovementsCount
        if (movesAwayFromGoal > 0) {
            mutableState.update { uiState ->
                uiState.copy(
                    snackbar = GameUiState.Snackbar.MovesAwayFromGoal(
                        moves = movesAwayFromGoal
                    )
                )
            }
        }
    }

    private fun onTimerTick(duration: Duration) {
        mutableState.update { uiState ->
            uiState.copy(
                stats = uiState.stats.copy(
                    timer = duration.formatTime(),
                    isPaused = false,
                ),
                fab = GameUiState.Fab.Pause,
            )
        }
    }

    private fun pause() {
        timer.pause()
        mutableState.update { uiState ->
            uiState.copy(
                stats = uiState.stats.copy(isPaused = true),
                fab = GameUiState.Fab.Resume,
            )
        }
    }

    private fun newGame() {
        timer.stop()
        puzzleGame = getSolvableGame()
        mutableState.update {
            GameUiState(
                board = GameUiState.Board(
                    tiles = puzzleGame.state.tiles,
                    columns = puzzleGame.state.matrixSize,
                    isEnabled = true,
                ),
            )
        }
    }
}

private fun getSolvableGame(): PuzzleGame {
    var solvableGame: PuzzleGame? = null
    while (solvableGame == null) {
        try {
            solvableGame = PuzzleGame(initialState = Puzzle3x3States.Shuffled)
        } catch (_: Exception) {
            // Ignore
        }
    }
    return solvableGame
}
