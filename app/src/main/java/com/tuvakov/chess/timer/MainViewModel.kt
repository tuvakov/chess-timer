package com.tuvakov.chess.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

// TODO: Timer is currently running in the main thread. Move it to a background thread.

class MainViewModel : ViewModel() {

    private var whitePlayer = MutableStateFlow(TimeUnit.MINUTES.toMillis(10))
    private var blackPlayer = MutableStateFlow(TimeUnit.MINUTES.toMillis(10))

    val whitePlayerTimer = whitePlayer.map { formatTime(it) }
    val blackPlayerTimer = blackPlayer.map { formatTime(it) }

    // 0 = white, 1 = black
    private var currentPlayer = 0

    // game state 0 = not started, 1 = playing, 2 = paused, 3 = stopped/ended
    private var gameState = 0

    init {
        setupTimer()
    }

    private fun setupTimer() {
        tickerFlow(interval = 200).onEach {

            if (gameState != 1) return@onEach

            if (currentPlayer == 0) {
                val remainingTime = whitePlayer.value - 200
                whitePlayer.emit(remainingTime)
            } else {
                val remainingTime = blackPlayer.value - 200
                blackPlayer.emit(remainingTime)
            }

            // TODO: If time runs out either of the players end the game.

        }.launchIn(viewModelScope)
    }

    fun changePlayer(player: Int) {
        if (gameState == 1) currentPlayer = player
    }

    fun setGameState(state: Int) {
        gameState = state
    }

    private fun tickerFlow(
        interval: Long = 200,
        unit: TimeUnit = TimeUnit.MILLISECONDS
    ) = flow {
        val intervalInMillis = unit.toMillis(interval)
        while (true) {
            emit(Unit)
            delay(intervalInMillis)
        }
    }

    private fun formatTime(millis: Long): String {
        val inSeconds = TimeUnit.MILLISECONDS.toSeconds(millis)
        val minutes = inSeconds / 60
        val seconds = inSeconds % 60
        val mm = if (minutes == 0L) "00" else minutes
        val ss = if (seconds == 0L) "00" else seconds
        return "$mm : $ss"
    }
}

