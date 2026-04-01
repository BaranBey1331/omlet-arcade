package com.omlet.arcade

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.omlet.arcade.ui.screens.HomeScreen
import com.omlet.arcade.ui.theme.OmletArcadeTheme

@Composable
fun App() {
    OmletArcadeTheme {
        Navigator(HomeScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}
