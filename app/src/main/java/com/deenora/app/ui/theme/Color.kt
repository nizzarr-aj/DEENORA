package com.deenora.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.Modifier

// Luminous luxury palette: pearl, royal sapphire and champagne gold.
val EmeraldDark = Color(0xFF101B36)
val EmeraldPrimary = Color(0xFF244A91)
val EmeraldMedium = Color(0xFF3768B8)
val EmeraldLight = Color(0xFF6F96DC)
val EmeraldPale = Color(0xFFE8EFFB)
val EmeraldGlow = Color(0xFF9BB7EC)

val GoldPrimary = Color(0xFFC6A15B)
val GoldLight = Color(0xFFEED9A5)
val GoldDark = Color(0xFF82632F)
val GoldContainer = Color(0xFFF8F1E3)
val GoldOnContainer = Color(0xFF49391D)

val SandBackground = Color(0xFFF7F8FC)
val SandSurface = Color(0xFFFFFEFC)
val SandSurfaceVariant = Color(0xFFEEF1F7)
val SandBorder = Color(0xFFDCE2ED)
val TextDark = Color(0xFF17213A)
val TextMuted = Color(0xFF68738A)

val DarkBackground = Color(0xFF0B1224)
val DarkSurface = Color(0xFF151F37)
val DarkSurfaceVariant = Color(0xFF202D49)
val DarkBorder = Color(0xFF354563)
val TextLight = Color(0xFFF5F6FA)
val TextLightMuted = Color(0xFFB5C0D5)


/** Calm pearl background with a subtle sapphire header glow. */
fun Modifier.deenkPageBackground(): Modifier = this.drawBehind {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colorStops = arrayOf(
                0f to Color(0xFFEAF0F8),
                0.22f to Color(0xFFF4F6FA),
                1f to Color(0xFFFFFEFC)
            ),
            startY = 0f,
            endY = h
        )
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x22C6A15B), Color.Transparent),
            center = Offset(w * 0.88f, h * 0.08f),
            radius = w * 0.72f
        ),
        radius = w * 0.72f,
        center = Offset(w * 0.88f, h * 0.08f)
    )
}
