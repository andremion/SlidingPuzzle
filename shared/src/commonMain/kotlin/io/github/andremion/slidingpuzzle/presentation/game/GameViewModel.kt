package io.github.andremion.slidingpuzzle.presentation.game

import io.github.andremion.slidingpuzzle.domain.puzzle.Puzzle3x3States
import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleGame
import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleState
import io.github.andremion.slidingpuzzle.domain.puzzle.getSolvableStates
import io.github.andremion.slidingpuzzle.domain.time.Timer
import io.github.andremion.slidingpuzzle.domain.time.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimePeriod
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.time.Duration

class GameViewModel : ViewModel() {

    private var game = PuzzleGame(initialState = Puzzle3x3States.Shuffled)
    private val timer = Timer(coroutineScope = viewModelScope)

    private val initialState: GameUiState
        get() = GameUiState(
            moves = game.moves.toString(),
            tiles = game.state.transform(),
            columns = game.state.matrixSize,
        )
    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<GameUiState> = mutableState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = initialState
        )

    fun onUiEvent(event: GameUiEvent) {
        when (event) {
            is GameUiEvent.TileClick -> {
                runCatching { game.move(tile = event.tile.number) }
                    .onSuccess {
                        timer.start(::onTimerTick)
                        mutableState.update { uiState ->
                            uiState.copy(
                                moves = game.moves.toString(),
                                tiles = game.state.transform(),
                            )
                        }
                    }
            }

            GameUiEvent.PauseClick -> {
                timer.pause()
                mutableState.update { uiState ->
                    uiState.copy(
                        fab = GameUiState.Fab.Resume,
                        blinkTimer = true
                    )
                }
            }

            GameUiEvent.ResumeClick -> {
                timer.resume(::onTimerTick)
            }

            GameUiEvent.ReplayClick -> {
                timer.stop()
                game = PuzzleGame(initialState = Puzzle3x3States.Shuffled)
                mutableState.update { initialState }
            }

            GameUiEvent.SolveClick -> {
                viewModelScope.launch {
                    runCatching { game.solve() }
                        .onSuccess { states ->
                            states.forEach { state ->
                                mutableState.update { uiState ->
                                    uiState.copy(
                                        tiles = state.transform(),
                                    )
                                }
                                delay(1_000)
                            }
                        }
                }
            }
        }
    }

    private fun onTimerTick(duration: Duration) {
        mutableState.update { uiState ->
            DateTimePeriod().toString()
            uiState.copy(
                timer = duration.formatTime(),
                fab = GameUiState.Fab.Pause,
                blinkTimer = false
            )
        }
    }
}
