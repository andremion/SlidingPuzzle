package io.github.andremion.slidingpuzzle.presentation.di

import io.github.andremion.slidingpuzzle.presentation.game.GameViewModel
import org.koin.dsl.module

object PresentationModule {
    val module = module {
        factory {
            GameViewModel()
        }
    }
}
