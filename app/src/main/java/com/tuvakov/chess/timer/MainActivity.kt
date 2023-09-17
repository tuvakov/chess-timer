package com.tuvakov.chess.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
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
//                    MainScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}