package io.github.andremion.slidingpuzzle.presentation

sealed interface GameUiEvent {
    data object ShuffleClick : GameUiEvent
    data object SolveClick : GameUiEvent
}
