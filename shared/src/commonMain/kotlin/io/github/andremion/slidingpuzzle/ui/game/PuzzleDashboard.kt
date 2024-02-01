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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.ui.component.VerticalDivider

@Composable
fun PuzzleDashboard(
    moves: String,
    timer: String,
    isPaused: Boolean
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
            isPaused = isPaused
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
            text = moves
        )
    }
}

@Composable
private fun Timer(
    modifier: Modifier,
    timer: String,
    isPaused: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "TimerPaused")
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPaused) 0.2f else 1f,
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
            text = timer
        )
    }
}
