package com.tuvakov.chess.timer.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class GameController(
    private val ticker: Ticker
) {

    private var currentPlayer = MutableStateFlow(Player.LIGHT)

    private var gameState = MutableStateFlow(GameState.NOT_STARTED)

    val gameUpdates = combine(
        ticker.ticker, currentPlayer, gameState
    ) { tick, player, state ->

        if (state == GameState.PAUSED || state == GameState.STOPPED) {
        }
    }


    fun start() {
        gameState.value = GameState.PLAYING
        ticker.start()
    }

    fun pause() {
        gameState.value = GameState.PAUSED
        ticker.stop()
    }

    fun resume() {
        gameState.value = GameState.PLAYING
        ticker.start()
    }

    fun stop() {
        gameState.value = GameState.STOPPED
        ticker.stop()
    }

    fun changePlayer(player: Player) {
        currentPlayer.value = player
    }
}

enum class Player {
    LIGHT, DARK
}

enum class GameState {
    NOT_STARTED, PLAYING, PAUSED, STOPPED, ENDED
}

data class GameScreen(
    val gameState: GameState,
    val currentPlayer: Player,
    val lightElapsed: Long,
    val darkElapsed: Long,
)