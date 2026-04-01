package com.omlet.arcade.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val WorkstationBlack = Color(0xFF000000)
val WorkstationDark = Color(0xFF0A0A0C)
val WorkstationGray = Color(0xFF141417)
val WorkstationBorder = Color(0xFF1F1F23)
val WorkstationText = Color(0xFFEFEFEF)
val TwitchAccent = Color(0xFF9146FF)

val OmletTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.sp,
        color = WorkstationText
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = WorkstationText
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = Color(0xFFADADB8)
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif, // Wikipedia-style for content
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = WorkstationText
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    )
)

val OmletColorScheme = darkColorScheme(
    primary = TwitchAccent,
    secondary = Color.White,
    background = WorkstationBlack,
    surface = WorkstationGray,
    outline = WorkstationBorder,
    onPrimary = Color.White,
    onSurface = WorkstationText
)

@Composable
fun OmletArcadeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OmletColorScheme,
        typography = OmletTypography,
        content = content
    )
}
