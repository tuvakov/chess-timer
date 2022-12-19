package com.tuvakov.chess.timer

import android.os.Bundle
import android.text.format.Time
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tuvakov.chess.timer.ui.theme.ChessTimerTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChessTimerTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    TimerScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel
                    )
                }
            }
        }
    }
}