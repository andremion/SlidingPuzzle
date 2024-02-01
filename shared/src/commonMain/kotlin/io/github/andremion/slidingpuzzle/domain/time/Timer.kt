package io.github.andremion.slidingpuzzle.domain.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

class Timer(
    private val coroutineScope: CoroutineScope
) {

    private var startTime: Instant? = null
    private var pauseTime: Instant? = null
    private var timerJob: Job? = null
    private val isTicking: Boolean
        get() = timerJob?.isActive == true
    val duration: Duration?
        get() = startTime?.let { time -> Clock.System.now() - time }

    fun start(onTick: (duration: Duration) -> Unit) {
        if (startTime == null) {
            startTime = Clock.System.now()
        }
        if (!isTicking) {
            resume(onTick)
        }
    }

    fun resume(onTick: (duration: Duration) -> Unit) {
        if (pauseTime != null) {
            startTime = startTime?.minus(pauseTime!! - Clock.System.now())
        }
        timerJob = coroutineScope.launch {
            while (true) {
                val duration = Clock.System.now() - startTime!!
                onTick(duration)
                delay(1_000)
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        pauseTime = Clock.System.now()
    }

    fun stop() {
        timerJob?.cancel()
        startTime = null
        pauseTime = null
    }
}
