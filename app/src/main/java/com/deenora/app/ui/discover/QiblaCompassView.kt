package com.deenora.app.ui.discover

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.location.LocationHelper
import com.deenora.app.ui.components.LocationSelectorDialog
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QiblaCompassView(
    viewModel: DiscoverViewModel,
    currentLanguage: AppLanguage,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showLocationSelector by remember { mutableStateOf(false) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    DisposableEffect(Unit) {
        viewModel.initSensors(context)
        onDispose {
            viewModel.releaseSensors()
        }
    }

    // Smooth compass rotation animation
    val animatedAzimuth by animateFloatAsState(
        targetValue = state.compassAzimuth,
        animationSpec = tween(durationMillis = 200),
        label = "compass_azimuth"
    )

    val relativeQiblaAngle = (state.qiblaBearing - animatedAzimuth + 360f) % 360f

    val haloColor by animateColorAsState(
        targetValue = if (state.isQiblaAligned) EmeraldGlow else Color.Transparent,
        animationSpec = tween(250),
        label = "halo_color"
    )

    val turnText = when (currentLanguage) {
        AppLanguage.ARABIC -> state.turnDirectionAr
        AppLanguage.FRENCH -> state.turnDirectionFr
        AppLanguage.ENGLISH -> state.turnDirectionEn
    }

    val cityName = when (currentLanguage) {
        AppLanguage.ARABIC -> state.settings.city.nameAr
        AppLanguage.FRENCH -> state.settings.city.nameFr
        AppLanguage.ENGLISH -> state.settings.city.nameEn
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.qiblaCompass(currentLanguage),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("qibla_back_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Quick GPS Refresh Button
                    IconButton(
                        onClick = {
                            if (locationPermissionsState.allPermissionsGranted) {
                                viewModel.refreshLocationWithGps(context)
                            } else {
                                locationPermissionsState.launchMultiplePermissionRequest()
                            }
                        },
                        modifier = Modifier.testTag("qibla_gps_button")
                    ) {
                        if (state.isGpsDetecting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(
                                imageVector = if (state.settings.city.isGps) Icons.Default.GpsFixed else Icons.Default.MyLocation,
                                contentDescription = "GPS",
                                tint = EmeraldPrimary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Badge & Selector Chip
            Surface(
                onClick = { showLocationSelector = true },
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight.copy(alpha = 0.4f)),
                modifier = Modifier.testTag("qibla_location_badge")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (state.settings.city.isGps) Icons.Default.GpsFixed else Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (state.settings.city.isGps) EmeraldPrimary else GoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "$cityName • ${String.format("%.2f", state.settings.city.latitude)}°, ${String.format("%.2f", state.settings.city.longitude)}°",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Change",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Direction Guidance Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("qibla_status_banner"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.isQiblaAligned) EmeraldPale else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (state.isQiblaAligned) EmeraldGlow else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (state.isQiblaAligned) EmeraldPrimary else GoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (state.isQiblaAligned) Icons.Filled.Check else Icons.Default.Navigation,
                            contentDescription = null,
                            tint = if (state.isQiblaAligned) Color.White else GoldDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = turnText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (state.isQiblaAligned) EmeraldDark else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (state.isQiblaAligned) {
                                when (currentLanguage) {
                                    AppLanguage.ARABIC -> "جاهز للصلاة • تقبل الله طاعتكم"
                                    AppLanguage.ENGLISH -> "Aligned for Prayer • May Allah accept"
                                    AppLanguage.FRENCH -> "Aligné pour la prière • Qu'Allah accepte"
                                }
                            } else {
                                AppStrings.alignQiblaTip(currentLanguage)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Compass Dial Canvas
            Box(
                modifier = Modifier
                    .size(290.dp)
                    .clip(CircleShape)
                    .border(5.dp, haloColor, CircleShape)
                    .shadow(16.dp, CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                // Dial Canvas with degree ticks, Cardinal letters (N, E, S, W)
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(-animatedAzimuth)
                ) {
                    val radius = size.minDimension / 2
                    val center = Offset(size.width / 2, size.height / 2)

                    // Outer golden rim
                    drawCircle(
                        color = Color(0xFFC8A236).copy(alpha = 0.35f),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Draw 36 dial ticks
                    for (i in 0 until 36) {
                        val angleRad = Math.toRadians(i * 10.0)
                        val isCardinal = i % 9 == 0
                        val tickLength = if (isCardinal) 16.dp.toPx() else 7.dp.toPx()
                        val strokeWidth = if (isCardinal) 3.dp.toPx() else 1.5.dp.toPx()
                        val tickColor = when (i) {
                            0 -> Color(0xFFE53935) // North is Red
                            else -> Color(0xFF888888).copy(alpha = 0.5f)
                        }

                        val start = Offset(
                            x = center.x + (radius - tickLength) * sin(angleRad).toFloat(),
                            y = center.y - (radius - tickLength) * cos(angleRad).toFloat()
                        )
                        val end = Offset(
                            x = center.x + radius * sin(angleRad).toFloat(),
                            y = center.y - radius * cos(angleRad).toFloat()
                        )
                        drawLine(color = tickColor, start = start, end = end, strokeWidth = strokeWidth)
                    }
                }

                // Dedicated Kaaba Pointer Needle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(relativeQiblaAngle),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        // Kaaba Icon Badge
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (state.isQiblaAligned) EmeraldPrimary else Color(0xFF1E1E1E))
                                .border(1.5.dp, GoldPrimary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = "Kaaba",
                                tint = GoldLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Direction Arrow Pointer
                        Canvas(modifier = Modifier.size(18.dp, 30.dp)) {
                            val path = Path().apply {
                                moveTo(size.width / 2, 0f)
                                lineTo(size.width, size.height)
                                lineTo(size.width / 2, size.height * 0.72f)
                                lineTo(0f, size.height)
                                close()
                            }
                            drawPath(
                                path = path,
                                color = if (state.isQiblaAligned) EmeraldGlow else GoldPrimary
                            )
                        }
                    }
                }

                // Compass Center Pivot
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary)
                        .border(2.5.dp, Color.White, CircleShape)
                )
            }

            // Info Cards: Bearing, Heading, Distance to Kaaba
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.qiblaBearing.toInt()}°",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = AppStrings.qibla(currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(42.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.compassAzimuth.toInt()}°",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.ARABIC -> "اتجاه الهاتف"
                                AppLanguage.ENGLISH -> "Heading"
                                AppLanguage.FRENCH -> "Orientation"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(42.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.distanceToKaabaKm.toInt()} km",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        )
                        Text(
                            text = AppStrings.distanceToKaaba(currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Location Selector Dialog
    if (showLocationSelector) {
        LocationSelectorDialog(
            currentCity = state.settings.city,
            currentLanguage = currentLanguage,
            onCitySelected = { city ->
                viewModel.setCity(city)
            },
            onDismissRequest = { showLocationSelector = false }
        )
    }
}
