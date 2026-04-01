package com.omlet.arcade.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun TwitchPlayer(channel: String, modifier: Modifier = Modifier)
