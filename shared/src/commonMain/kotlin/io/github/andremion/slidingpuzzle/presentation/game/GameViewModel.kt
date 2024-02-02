package io.github.andremion.slidingpuzzle.presentation.game

import io.github.andremion.slidingpuzzle.domain.puzzle.Puzzle3x3States
import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleGame
import io.github.andremion.slidingpuzzle.domain.time.Timer
import io.github.andremion.slidingpuzzle.domain.time.formatTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.time.Duration

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
                viewModelScope.launch {
                    // states will also contain the current state (first item) and the goal state (last item)
                    val states = puzzleGame.solve()
                    if (states.size > 1) {
                        puzzleGame.replace(newState = states[1])
                        if (states.size > 2) {
                            mutableState.update { uiState ->
                                uiState.copy(
                                    snackbar = GameUiState.Snackbar.MovesAwayFromGoal(
                                        moves = states.size - 2
                                    )
                                )
                            }
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
                    uiState.copy(
                        dialog = GameUiState.Dialog.None
                    )
                }
            }

            GameUiEvent.DismissSnackbar -> {
                mutableState.update { uiState ->
                    uiState.copy(
                        snackbar = GameUiState.Snackbar.None
                    )
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
                    )
                )
            }
        } else {
            timer.start(::onTimerTick)
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
