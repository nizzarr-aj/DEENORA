package com.deenora.app.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.deenora.app.data.prayer.CityLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale
import java.util.TimeZone

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation(
        context: Context,
        onSuccess: (CityLocation) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            onError("Location permission not granted")
            return
        }

        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        resolveCityFromLocation(context, location, onSuccess)
                    } else {
                        // Fallback to LocationManager
                        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                        val lastGps = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        val lastNetwork = lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        val best = lastGps ?: lastNetwork
                        if (best != null) {
                            resolveCityFromLocation(context, best, onSuccess)
                        } else {
                            onError("Unable to retrieve device coordinates")
                        }
                    }
                }
                .addOnFailureListener {
                    // Fallback to LocationManager
                    val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    val lastGps = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val lastNetwork = lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val best = lastGps ?: lastNetwork
                    if (best != null) {
                        resolveCityFromLocation(context, best, onSuccess)
                    } else {
                        onError(it.localizedMessage ?: "Failed to retrieve location")
                    }
                }
        } catch (e: Exception) {
            onError(e.localizedMessage ?: "Error fetching location")
        }
    }

    private fun resolveCityFromLocation(
        context: Context,
        location: Location,
        onSuccess: (CityLocation) -> Unit
    ) {
        val lat = location.latitude
        val lng = location.longitude
        val tzHours = TimeZone.getDefault().rawOffset / 3600000.0

        var cityName = "Current Location"
        var countryName = "GPS"

        try {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    cityName = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: "My Location"
                    countryName = addr.countryName ?: ""
                }
            }
        } catch (_: Exception) {}

        val result = CityLocation(
            nameEn = cityName,
            nameAr = cityName,
            nameFr = cityName,
            countryEn = countryName,
            countryAr = countryName,
            latitude = lat,
            longitude = lng,
            timezoneOffsetHours = tzHours,
            isGps = true
        )
        onSuccess(result)
    }
}
