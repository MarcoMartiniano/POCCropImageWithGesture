package com.marco.poccropimagewithgesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.marco.poccropimagewithgesture.ui.main.MainScreenFactory
import com.marco.poccropimagewithgesture.ui.theme.POCCropImageWithGestureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            POCCropImageWithGestureTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreenFactory(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}