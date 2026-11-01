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


/** Shared night-sky and warm pearl backdrop used across every main Deenora page. */
fun Modifier.deenkPageBackground(): Modifier = this.drawBehind {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colorStops = arrayOf(
                0f to Color(0xFF04152F),
                0.20f to Color(0xFF0A2B55),
                0.39f to Color(0xFF173E69),
                0.43f to Color(0xFFF5EDE2),
                1f to Color(0xFFF7F4EF)
            ),
            startY = 0f,
            endY = h
        )
    )
    for (i in 0 until 90) {
        val x = ((i * 73) % 997) / 997f * w
        val y = ((i * 47) % 389) / 389f * h * 0.39f
        val r = if (i % 9 == 0) 2f else 0.8f
        drawCircle(Color(0xFFFFE6AD).copy(alpha = if (i % 4 == 0) 0.85f else 0.42f), r, Offset(x, y))
    }
    val moon = Offset(w * 0.20f, h * 0.115f)
    drawCircle(Color(0xFFFFD989), w * 0.052f, moon)
    drawCircle(Color(0xFF0A2B55), w * 0.048f, Offset(moon.x + w * 0.025f, moon.y - h * 0.009f))
    val base = h * 0.405f
    drawRect(Color(0xFF061B37), Offset(0f, base), Size(w, h * 0.045f))
    drawRect(Color(0xFF0A2446), Offset(w * 0.12f, base - h * 0.075f), Size(w * 0.76f, h * 0.075f))
    drawCircle(Color(0xFF0A2446), w * 0.105f, Offset(w * 0.5f, base - h * 0.075f))
    drawRect(Color(0xFF0A2446), Offset(w * 0.46f, base - h * 0.16f), Size(w * 0.08f, h * 0.085f))
    drawCircle(Color(0xFF0A2446), w * 0.04f, Offset(w * 0.5f, base - h * 0.16f))
    for (x in listOf(0.12f, 0.82f)) {
        drawRect(Color(0xFF0A2446), Offset(w * x, base - h * 0.14f), Size(w * 0.045f, h * 0.14f))
        drawCircle(Color(0xFF0A2446), w * 0.023f, Offset(w * (x + 0.022f), base - h * 0.14f))
    }
}
