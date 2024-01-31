package io.github.andremion.slidingpuzzle.di

import io.github.andremion.slidingpuzzle.presentation.PresentationModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

internal fun initDI(): KoinApplication =
    startKoin {
        modules(
            PresentationModule.module
        )
    }
