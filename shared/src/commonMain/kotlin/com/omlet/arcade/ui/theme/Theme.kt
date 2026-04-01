package com.omlet.arcade.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CyberBlack = Color(0xFF050505)
val CyberDarkGray = Color(0xFF111111)
val CyberLightGray = Color(0xFF222222)
val CyberWhite = Color(0xFFF0F0F0)
val CyberAccent = Color(0xFF9146FF) // Twitch Purple for accents

val OmletTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        letterSpacing = (-0.5).sp,
        color = CyberWhite
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = CyberWhite
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Changed to SansSerif for UI as per Cyber-Professional
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = Color(0xFFAAAAAA)
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif, // Serif kept for content/articles
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = CyberWhite
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        color = CyberWhite
    )
)

val OmletColorScheme = darkColorScheme(
    primary = CyberAccent,
    secondary = CyberLightGray,
    background = CyberBlack,
    surface = CyberDarkGray,
    onPrimary = Color.White,
    onSecondary = CyberWhite,
    onBackground = CyberWhite,
    onSurface = CyberWhite
)

@Composable
fun OmletArcadeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OmletColorScheme,
        typography = OmletTypography,
        content = content
    )
}
