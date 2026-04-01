package com.omlet.arcade.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.unit.sp

val CyberDark = Color(0xFF0F0F13)
val NeonPink = Color(0xFFFF0055)
val ElectricCyan = Color(0xFF00E5FF)
val CardSurface = Color(0xFF1E1E26)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA0A0AB)

val OmletTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextSecondary
    )
)

val OmletColorScheme = darkColorScheme(
    primary = NeonPink,
    secondary = ElectricCyan,
    background = CyberDark,
    surface = CardSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun OmletArcadeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OmletColorScheme,
        typography = OmletTypography,
        content = content
    )
}
