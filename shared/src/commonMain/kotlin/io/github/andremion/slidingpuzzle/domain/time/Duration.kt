package io.github.andremion.slidingpuzzle.domain.time

import kotlin.time.Duration

fun Duration.formatTime(): String {
    val hours = inWholeHours
    val minutes = inWholeMinutes % 60
    val seconds = inWholeSeconds % 60
    return "${hours.padded()}:${minutes.padded()}:${seconds.padded()}"
}

private fun Long.padded(): String =
    toString().padStart(length = 2, padChar = '0')
