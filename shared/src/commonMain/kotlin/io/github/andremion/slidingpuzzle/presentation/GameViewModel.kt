package io.github.andremion.slidingpuzzle.presentation

import io.github.andremion.slidingpuzzle.domain.puzzle.Puzzle3x3States
import io.github.andremion.slidingpuzzle.domain.puzzle.PuzzleGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class GameViewModel : ViewModel() {

    private var game = PuzzleGame(state = Puzzle3x3States.Shuffled)

    private val initialState = GameUiState(
        pieces = game.state.transform(),
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
            GameUiEvent.ShuffleClick -> {
                game = PuzzleGame(state = Puzzle3x3States.Shuffled)
                mutableState.update { uiState ->
                    uiState.copy(
                        pieces = game.state.transform(),
                        columns = game.state.matrixSize,
                    )
                }
            }

            GameUiEvent.SolveClick -> {
                viewModelScope.launch {
                    val states = game.solve()
                    states.forEach { state ->
                        mutableState.update { uiState ->
                            uiState.copy(
                                pieces = state.transform(),
                            )
                        }
                        delay(1_000)
                    }
                }
            }
        }
    }
}
