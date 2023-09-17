package com.tuvakov.chess.timer.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed interface TimerState {

    data class Paused(
        val elapsedTime: Long
    ) : TimerState

    data class Running(
        val startTime: Long,
        val elapsedTime: Long
    ) : TimerState
}

interface TimestampProvider {

    fun getMilliseconds(): Long
}

internal class TimerStateHolder(
    private val stopwatchStateCalculator: TimerStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter,
) {

    var currentState: TimerState = TimerState.Paused(0)
        private set

    fun start() {
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = TimerState.Paused(0)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is TimerState.Paused -> currentState.elapsedTime
            is TimerState.Running -> elapsedTimeCalculator.calculate(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}

class TimerStateCalculator(
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
) {

    fun calculateRunningState(oldState: TimerState): TimerState.Running =
        when (oldState) {
            is TimerState.Running -> oldState
            is TimerState.Paused -> {
                TimerState.Running(
                    startTime = timestampProvider.getMilliseconds(),
                    elapsedTime = oldState.elapsedTime
                )
            }
        }

    fun calculatePausedState(oldState: TimerState): TimerState.Paused =
        when (oldState) {
            is TimerState.Running -> {
                val elapsedTime = elapsedTimeCalculator.calculate(oldState)
                TimerState.Paused(elapsedTime = elapsedTime)
            }

            is TimerState.Paused -> oldState
        }
}

class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider,
) {

    fun calculate(state: TimerState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }
}

internal class TimestampMillisecondsFormatter {

    companion object {
        const val DEFAULT_TIME = "00:00:000"
    }

    fun format(timestamp: Long): String {
        val millisecondsFormatted = (timestamp % 1000).pad(3)
        val seconds = timestamp / 1000
        val secondsFormatted = (seconds % 60).pad(2)
        val minutes = seconds / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hours = minutes / 60
        return if (hours > 0) {
            val hoursFormatted = (minutes / 60).pad(2)
            "$hoursFormatted:$minutesFormatted:$secondsFormatted"
        } else {
            "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')
}

internal class StopwatchListOrchestrator(
    private val stopwatchStateHolder: TimerStateHolder,
    private val scope: CoroutineScope,
) {

    private var job: Job? = null
    private val mutableTicker = MutableStateFlow("")
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        if (job == null) startJob()
        stopwatchStateHolder.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                mutableTicker.value = stopwatchStateHolder.getStringTimeRepresentation()
                delay(20)
            }
        }
    }

    fun pause() {
        stopwatchStateHolder.pause()
        stopJob()
    }

    fun stop() {
        stopwatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun clearValue() {
        mutableTicker.value = ""
    }
}
