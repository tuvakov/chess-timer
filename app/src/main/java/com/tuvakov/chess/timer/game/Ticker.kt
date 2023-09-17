package com.tuvakov.chess.timer.game

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TODO: How to expose the tick properly?

class Ticker(
    private val interval: Long = DEFAULT_INTERVAL,
    private val scope: CoroutineScope,
) {

    private var job: Job? = null

    private val _ticker = MutableStateFlow(Unit)
    val ticker: StateFlow<Unit> = _ticker

    fun start() {
        if (job != null) return
        job = scope.launch {
            while (isActive) {
                _ticker.value = Unit
                delay(interval)
            }
        }
    }

    fun stop() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    companion object {
        const val DEFAULT_INTERVAL = 20L
    }
}