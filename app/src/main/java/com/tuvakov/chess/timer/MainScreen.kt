package com.tuvakov.chess.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val presets = getPresets()

    Column(modifier = modifier.padding(8.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(text = "Chess Timer", style = MaterialTheme.typography.h4)

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Timer Preset Icon"
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp)
        ) {
            for (preset in presets) {
                item {
                    PresetCard(
                        modifier = Modifier.padding(4.dp),
                        preset = preset,
                        onClick = {}
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        PresetCard(
            modifier = Modifier.fillMaxWidth(),
            preset = TimerPreset("Custom", "00:00"),
            onClick = {}
        )
    }
}

@Composable
private fun PresetCard(
    modifier: Modifier = Modifier,
    preset: TimerPreset,
    onClick: () -> Unit
) {
    Card(modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Rounded.Timer,
                contentDescription = "Timer Preset Icon"
            )
            Spacer(modifier = Modifier.size(4.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = preset.name, style = MaterialTheme.typography.h5)
                Text(text = preset.time, style = MaterialTheme.typography.h5)
            }
        }
    }
}

@Preview
@Composable
fun PresetCardPrev() {
    val preset = TimerPreset(name = "Bullet", "2:00")
    PresetCard(preset = preset, onClick = {})
}

/* TODO: Put somewhere else */

data class TimerPreset(
    val name: String,
    val time: String
)

private fun getPresets(): List<TimerPreset> {
    return listOf(
        TimerPreset(name = "Bullet", "1:00"),
        TimerPreset(name = "Bullet", "2:00"),
        TimerPreset(name = "Blitz", "3:00"),
        TimerPreset(name = "Blitz", "5:00"),
        TimerPreset(name = "Rapid", "10:00"),
        TimerPreset(name = "Classical", "15:00"),
    )
}