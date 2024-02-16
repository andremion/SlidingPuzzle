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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.ui.component.VerticalDivider

@Composable
fun PuzzleStats(
    moves: String,
    timer: String,
    blinkTimer: Boolean
) {
    Row(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Moves(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
            moves = moves,
        )
        VerticalDivider()
        Timer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
            timer = timer,
            blink = blinkTimer
        )
    }
}

@Composable
private fun Moves(
    modifier: Modifier,
    moves: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Numbers,
            contentDescription = "Moves"
        )
        Text(
            text = moves,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun Timer(
    modifier: Modifier,
    timer: String,
    blink: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BlinkingTimer")
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (blink) 0.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Row(
        modifier = modifier
            .alpha(animatedAlpha),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = "Timer"
        )
        Text(
            text = timer,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
