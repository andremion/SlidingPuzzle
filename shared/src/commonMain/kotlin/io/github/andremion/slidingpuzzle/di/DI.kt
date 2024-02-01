package io.github.andremion.slidingpuzzle.di

import io.github.andremion.slidingpuzzle.presentation.di.PresentationModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initDI(): KoinApplication =
    startKoin {
        modules(
            PresentationModule.module
        )
    }
