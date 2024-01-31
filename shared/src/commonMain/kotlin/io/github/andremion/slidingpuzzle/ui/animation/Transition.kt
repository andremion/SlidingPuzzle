package io.github.andremion.slidingpuzzle.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FadeAnimatedVisibility(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = scaleOut() + fadeOut(),
        content = content
    )
}
