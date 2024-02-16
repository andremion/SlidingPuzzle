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

package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

private val BoardInnerPadding = 8.dp
private val TileRoundedCornerSize = 8.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PuzzleBoard(
    modifier: Modifier,
    tiles: List<Int>,
    columns: Int,
    tileTextStyle: TextStyle,
    isEnabled: Boolean = true,
    onClick: ((tile: Int) -> Unit)? = null
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(BoardInnerPadding),
        horizontalArrangement = Arrangement.spacedBy(BoardInnerPadding),
        verticalArrangement = Arrangement.spacedBy(BoardInnerPadding),
    ) {
        tiles
            .chunked(columns)
            .forEach { row ->
                items(
                    items = row,
                    key = { it }
                ) { tile ->
                    BoardTile(
                        modifier = Modifier.animateItemPlacement(),
                        number = tile,
                        textStyle = tileTextStyle,
                        isEnabled = isEnabled,
                        onClick = { onClick?.invoke(tile) }
                    )
                }
            }
    }
}

@Composable
private fun BoardTile(
    modifier: Modifier,
    number: Int,
    textStyle: TextStyle,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        if (number > 0) {
            Button(
                modifier = Modifier
                    .aspectRatio(1f),
                shape = RoundedCornerShape(TileRoundedCornerSize),
                contentPadding = PaddingValues(0.dp),
                onClick = onClick,
                enabled = isEnabled
            ) {
                Text(
                    text = number.toString(),
                    style = textStyle
                )
            }
        }
    }
}
