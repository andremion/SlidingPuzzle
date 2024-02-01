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

    fun onUiEvent(event: GameUiEvent) {
        when (event) {
            is GameUiEvent.TileClick -> {
                runCatching { puzzleGame.move(tile = event.tile) }
                    .onSuccess {
                        timer.start(::onTimerTick)
                        mutableState.update { uiState ->
                            uiState.copy(
                                moves = puzzleGame.moves.toString(),
                                board = puzzleGame.state.transform(),
                            )
                        }
                    }.onFailure {
                        // Tile cannot be moved
                    }
            }

            GameUiEvent.PauseClick -> {
                timer.pause()
                mutableState.update { uiState ->
                    uiState.copy(
                        fab = GameUiState.Fab.Resume,
                        isPaused = true
                    )
                }
            }

            GameUiEvent.ResumeClick -> {
                timer.resume(::onTimerTick)
            }

            GameUiEvent.ReplayClick -> {
                timer.stop()
                puzzleGame = getSolvableGame()
                mutableState.update {
                    GameUiState(
                        moves = puzzleGame.moves.toString(),
                        board = puzzleGame.state.transform(),
                    )
                }
            }

            GameUiEvent.HintClick -> {
                viewModelScope.launch {
                    val states = puzzleGame.solve()
                    puzzleGame.replace(newState = states[1])
                    mutableState.update { uiState ->
                        uiState.copy(
                            moves = puzzleGame.moves.toString(),
                            board = puzzleGame.state.transform(),
                        )
                    }
                    timer.start(::onTimerTick)
                }
            }

            GameUiEvent.GoalClick -> {
                mutableState.update { uiState ->
                    uiState.copy(
                        dialog = GameUiState.Dialog.Goal(
                            board = puzzleGame.goal.transform()
                        )
                    )
                }
            }

            GameUiEvent.DismissDialogClick -> {
                mutableState.update { uiState ->
                    uiState.copy(
                        dialog = GameUiState.Dialog.None
                    )
                }
            }
        }
    }

    private fun onTimerTick(duration: Duration) {
        mutableState.update { uiState ->
            uiState.copy(
                timer = duration.formatTime(),
                fab = GameUiState.Fab.Pause,
                isPaused = false
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
