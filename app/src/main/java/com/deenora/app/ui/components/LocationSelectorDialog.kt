package com.deenora.app.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.location.LocationHelper
import com.deenora.app.data.prayer.CityLocation
import com.deenora.app.data.prayer.PredefinedCities
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.TimeZone

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSelectorDialog(
    currentCity: CityLocation,
    currentLanguage: AppLanguage,
    onCitySelected: (CityLocation) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isDetectingGps by remember { mutableStateOf(false) }
    var gpsStatusMessage by remember { mutableStateOf<String?>(null) }
    var showCustomCoordEntry by remember { mutableStateOf(false) }
    var hasRequestedLocation by remember { mutableStateOf(false) }

    var customName by remember { mutableStateOf("") }
    var customLat by remember { mutableStateOf("") }
    var customLng by remember { mutableStateOf("") }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted && hasRequestedLocation) {
            isDetectingGps = true
            gpsStatusMessage = "Acquiring GPS fix..."
            LocationHelper.fetchCurrentLocation(
                context = context,
                onSuccess = { loc ->
                    isDetectingGps = false
                    onCitySelected(loc)
                    onDismissRequest()
                },
                onError = { err ->
                    isDetectingGps = false
                    gpsStatusMessage = err
                }
            )
        }
    }

    val filteredCities = remember(searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) {
            PredefinedCities.CITIES
        } else {
            PredefinedCities.CITIES.filter {
                it.nameEn.lowercase().contains(q) ||
                it.nameAr.contains(q) ||
                it.nameFr.lowercase().contains(q) ||
                it.countryEn.lowercase().contains(q) ||
                it.countryAr.contains(q)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "اختيار المدينة والموقع"
                        AppLanguage.ENGLISH -> "Select Location & City"
                        AppLanguage.FRENCH -> "Choisir la Ville & Emplacement"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                // GPS Primary Button
                Button(
                    onClick = {
                        gpsStatusMessage = null
                        if (locationPermissionsState.allPermissionsGranted) {
                            isDetectingGps = true
                            gpsStatusMessage = "Detecting actual device location..."
                            LocationHelper.fetchCurrentLocation(
                                context = context,
                                onSuccess = { loc ->
                                    isDetectingGps = false
                                    onCitySelected(loc)
                                    onDismissRequest()
                                },
                                onError = { err ->
                                    isDetectingGps = false
                                    gpsStatusMessage = err
                                }
                            )
                        } else {
                            hasRequestedLocation = true
                            locationPermissionsState.launchMultiplePermissionRequest()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("use_current_gps_location_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isDetectingGps) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.MyLocation, contentDescription = null, tint = GoldLight)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.ARABIC -> "استخدام موقعي الفعلي (GPS)"
                            AppLanguage.ENGLISH -> "Use My Real Location (GPS)"
                            AppLanguage.FRENCH -> "Utiliser ma position réelle (GPS)"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }

                if (gpsStatusMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = gpsStatusMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            when (currentLanguage) {
                                AppLanguage.ARABIC -> "ابحث عن مدينة أو دولة..."
                                AppLanguage.ENGLISH -> "Search city or country..."
                                AppLanguage.FRENCH -> "Rechercher une ville..."
                            }
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("city_search_text_field")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Coordinate Option Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCustomCoordEntry = !showCustomCoordEntry }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.ARABIC -> "إدخال إحداثيات مخصصة (خط العرض والطول)"
                            AppLanguage.ENGLISH -> "Enter Custom Coordinates (Lat / Lng)"
                            AppLanguage.FRENCH -> "Coordonnées personnalisées (Lat / Lng)"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (showCustomCoordEntry) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                if (showCustomCoordEntry) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customName,
                            onValueChange = { customName = it },
                            label = { Text("City Name / Label") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = customLat,
                                onValueChange = { customLat = it },
                                label = { Text("Latitude") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = customLng,
                                onValueChange = { customLng = it },
                                label = { Text("Longitude") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Button(
                            onClick = {
                                val lat = customLat.toDoubleOrNull()
                                val lng = customLng.toDoubleOrNull()
                                if (lat != null && lng != null) {
                                    val name = customName.ifBlank { "Custom Location" }
                                    val tz = TimeZone.getDefault().rawOffset / 3600000.0
                                    val customLoc = CityLocation(
                                        nameEn = name,
                                        nameAr = name,
                                        nameFr = name,
                                        countryEn = "Custom",
                                        countryAr = "مخصص",
                                        latitude = lat,
                                        longitude = lng,
                                        timezoneOffsetHours = tz,
                                        isGps = false
                                    )
                                    onCitySelected(customLoc)
                                    onDismissRequest()
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldDark)
                        ) {
                            Text("Apply Coordinates")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List of Predefined World Cities
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredCities) { city ->
                        val isSelected = city.nameEn == currentCity.nameEn && !currentCity.isGps
                        val cityName = when (currentLanguage) {
                            AppLanguage.ARABIC -> city.nameAr
                            AppLanguage.FRENCH -> city.nameFr
                            AppLanguage.ENGLISH -> city.nameEn
                        }
                        val countryName = when (currentLanguage) {
                            AppLanguage.ARABIC -> city.countryAr
                            else -> city.countryEn
                        }

                        Surface(
                            onClick = {
                                onCitySelected(city)
                                onDismissRequest()
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) EmeraldPale else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .testTag("city_item_${city.nameEn.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) EmeraldPrimary else GoldContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else GoldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = cityName,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = "$countryName • ${String.format("%.2f", city.latitude)}°, ${String.format("%.2f", city.longitude)}°",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = EmeraldPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}
