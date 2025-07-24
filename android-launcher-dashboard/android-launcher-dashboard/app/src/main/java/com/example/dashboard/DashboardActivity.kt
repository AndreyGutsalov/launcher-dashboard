
package com.example.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.LocationManager
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class DashboardActivity : ComponentActivity() {
    private lateinit var timeText: TextView
    private lateinit var dateText: TextView
    private lateinit var weatherText: TextView
    private lateinit var compassText: TextView
    private lateinit var gpsText: TextView
    private lateinit var launchButton: Button

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        timeText = findViewById(R.id.timeText)
        dateText = findViewById(R.id.dateText)
        weatherText = findViewById(R.id.weatherText)
        compassText = findViewById(R.id.compassText)
        gpsText = findViewById(R.id.gpsText)
        launchButton = findViewById(R.id.launchButton)

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        timer(period = 1000) {
            runOnUiThread {
                val now = Date()
                timeText.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(now)
                dateText.text = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(now)
            }
        }

        val locationManager = getSystemService(LocationManager::class.java)
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 10f) { loc ->
            weatherText.text = "Lat: ${loc.latitude}, Lon: ${loc.longitude}"
        }

        locationManager?.registerGnssStatusCallback(object : GnssStatus.Callback() {
            override fun onSatelliteStatusChanged(status: GnssStatus) {
                gpsText.text = "Satellites: ${status.satelliteCount}"
            }
        })

        launchButton.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("com.android.vending")
            intent?.let { startActivity(it) }
        }
    }
}
