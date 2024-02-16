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

sealed interface GameUiEvent {
    data class TileClick(val tile: Int) : GameUiEvent
    data object PauseClick : GameUiEvent
    data object ResumeClick : GameUiEvent
    data object ReplayClick : GameUiEvent
    data object HintClick : GameUiEvent
    data object GoalClick : GameUiEvent
    data class DismissDialogClick(val dialog: GameUiState.Dialog) : GameUiEvent
    data object DismissSnackbar : GameUiEvent
}
