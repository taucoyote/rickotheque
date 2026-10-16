package com.example.rickotheque.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rickotheque.ui.navigation.AppNavigation
import com.example.rickotheque.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                RickAndMortyTheme {
                    AppNavigation()
                }
            }
        }
}