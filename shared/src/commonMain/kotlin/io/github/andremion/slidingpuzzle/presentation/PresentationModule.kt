package io.github.andremion.slidingpuzzle.presentation

import org.koin.dsl.module

object PresentationModule {
    val module = module {
        factory {
            GameViewModel()
        }
    }
}
