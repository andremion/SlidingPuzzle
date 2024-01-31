package io.github.andremion.slidingpuzzle.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andremion.slidingpuzzle.ui.component.VerticalDivider

@Composable
fun PuzzleDashboard(
    moves: String,
    timer: String
) {
    Row(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
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
        VerticalDivider()
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = "Timer"
            )
            Text(
                text = timer
            )
        }
    }
}
