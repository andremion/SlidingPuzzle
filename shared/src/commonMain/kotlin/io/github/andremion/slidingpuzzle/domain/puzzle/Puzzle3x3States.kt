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

package io.github.andremion.slidingpuzzle.domain.puzzle

object Puzzle3x3States {

    val Shuffled
        get() = PuzzleState(
            tiles = List(9) { it }
                .shuffled()
        )

    val Easy = PuzzleState(
        tiles = listOf(
            1, 3, 4,
            8, 6, 2,
            7, 0, 5
        )
    )

    val Medium = PuzzleState(
        tiles = listOf(
            2, 8, 1,
            0, 4, 3,
            7, 6, 5
        )
    )

    val Hard = PuzzleState(
        tiles = listOf(
            2, 8, 1,
            4, 6, 3,
            0, 7, 5
        )
    )

    val Hardest = PuzzleState(
        tiles = listOf(
            5, 6, 7,
            4, 0, 8,
            3, 2, 1
        )
    )
}
