package io.github.andremion.slidingpuzzle.domain.time

import kotlin.time.Duration

fun Duration.formatTime(): String =
    "${inWholeHours.padded()}:${inWholeMinutes.padded()}:${inWholeSeconds.padded()}"

private fun Long.padded(): String =
    toString().padStart(length = 2, padChar = '0')
