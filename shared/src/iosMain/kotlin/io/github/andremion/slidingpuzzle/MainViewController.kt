package io.github.andremion.slidingpuzzle

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
    ComposeUIViewController { SlidingPuzzle() }
