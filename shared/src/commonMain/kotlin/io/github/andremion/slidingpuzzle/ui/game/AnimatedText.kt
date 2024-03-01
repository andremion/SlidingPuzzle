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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedText(
    text: String,
    style: TextStyle
) {
    var oldText by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        oldText = newText
        newText = text
    }

    Row {
        text.forEach { char ->
            AnimatedContent(
                targetState = char.toString(),
                transitionSpec = {
                    if (newText > oldText) {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) togetherWith
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                    } else {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) togetherWith
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                    }
                },
            ) { state ->
                Text(
                    text = state,
                    style = style
                )
            }
        }
    }
}
