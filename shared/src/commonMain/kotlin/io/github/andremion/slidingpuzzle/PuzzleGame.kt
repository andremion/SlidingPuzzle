package io.github.andremion.slidingpuzzle

import androidx.compose.runtime.Composable
import io.github.andremion.slidingpuzzle.ui.PuzzleBoard
import io.github.andremion.slidingpuzzle.ui.theme.AppTheme
import moe.tlaster.precompose.PreComposeApp

@Composable
fun SlidingPuzzle() {
    PreComposeApp {
        AppTheme {
            PuzzleBoard()
        }
    }
}
