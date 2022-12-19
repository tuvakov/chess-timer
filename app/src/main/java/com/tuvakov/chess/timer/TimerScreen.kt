package com.tuvakov.chess.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// NOTE: Maybe one tone gradient that fades towards the middle would be great?
// NOTE: Maybe sure it's fullscreen.

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {

            val whitePlayerTimer = viewModel.whitePlayerTimer.collectAsState(initial = "")
            val blackPlayerTimer = viewModel.blackPlayerTimer.collectAsState(initial = "")

            TimerHalf(
                modifier = Modifier
                    .weight(0.5f)
                    .background(color = Color.Magenta.copy(alpha = 0.5f))
                    .rotate(180f),
                time = whitePlayerTimer.value,
                onClick = {
                    viewModel.changePlayer(0)
                }
            )
            TimerHalf(
                modifier = Modifier
                    .weight(0.5f)
                    .background(color = Color.Red.copy(alpha = 0.5f)),
                time = blackPlayerTimer.value,
                onClick = {
                    viewModel.changePlayer(1)
                }
            )
        }

        Buttons(
            onClick = { action ->
                val state = when (action) {
                    ButtonAction.START -> 1
                    ButtonAction.PAUSE -> 2
                    ButtonAction.RESUME -> 1
                    ButtonAction.STOP -> 3
                }
                viewModel.setGameState(state)
            }
        )
    }
}

@Composable
private fun TimerHalf(
    modifier: Modifier = Modifier,
    time: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = time, style = MaterialTheme.typography.h3)
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    onClick: (ButtonAction) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onClick(ButtonAction.START) }
        ) {
            Icon(
                modifier = Modifier.size(70.dp),
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play Button"
            )
        }
        Spacer(modifier = modifier.size(12.dp))
        IconButton(
            onClick = { onClick(ButtonAction.PAUSE) }
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Rounded.StopCircle,
                contentDescription = "Stop Button"
            )
        }
        Spacer(modifier = modifier.size(12.dp))
    }
}

private enum class ButtonAction {
    START, PAUSE, RESUME, STOP
}