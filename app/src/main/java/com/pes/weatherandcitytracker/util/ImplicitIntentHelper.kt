package com.pes.weatherandcitytracker.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.pes.weatherandcitytracker.R

/**
 * Utility helper encapsulating all Implicit Intents for the application.
 * Fulfills syllabus requirement #5f (minimum 2 implicit intents to open other apps).
 */
object ImplicitIntentHelper {

    /**
     * Implicit Intent 1: Opens the selected city's location in Google Maps (or any installed mapping app).
     * Action: Intent.ACTION_VIEW
     * URI: geo:lat,lon?q=lat,lon(cityName)
     * Fallback: Opens in browser if no native maps application is installed.
     */
    fun openCityOnMap(
        context: Context,
        latitude: Double,
        longitude: Double,
        cityName: String
    ) {
        val encodedCity = Uri.encode(cityName)
        val geoUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($encodedCity)")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            // Fallback to web browser Google Maps
            Toast.makeText(
                context,
                context.getString(R.string.error_no_map_app),
                Toast.LENGTH_SHORT
            ).show()

            val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
            openBrowserUrl(context, webUri.toString())
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Unable to open map: ${e.localizedMessage ?: "Unknown error"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Implicit Intent 2: Shares a formatted weather summary via Android system share sheet.
     * Action: Intent.ACTION_SEND
     * Type: text/plain
     * Behavior: Shows native Chooser dialog so user can share to WhatsApp, Telegram, Email, SMS, etc.
     */
    fun shareWeatherReport(
        context: Context,
        cityName: String,
        temperature: Double,
        tempUnit: String,
        conditionDescription: String,
        humidityPercentage: Int,
        windSpeed: Double,
        windUnit: String
    ) {
        val shareText = context.getString(
            R.string.share_weather_template,
            cityName,
            temperature,
            tempUnit,
            conditionDescription,
            humidityPercentage,
            windSpeed,
            windUnit
        )

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Weather update for $cityName")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooserTitle = context.getString(R.string.share_chooser_title)
        val chooserIntent = Intent.createChooser(sendIntent, chooserTitle).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Unable to share: ${e.localizedMessage ?: "No sharing app found"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Implicit Intent 3 (Bonus): Opens external web links (e.g. Open-Meteo documentation/attribution)
     * in the user's default web browser.
     * Action: Intent.ACTION_VIEW
     */
    fun openBrowserUrl(
        context: Context,
        url: String
    ) {
        val safeUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "https://$url"
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.error_no_browser),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Unable to open link: ${e.localizedMessage ?: "Unknown error"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Implicit Intent 4 (Bonus): Opens user's email client to send project feedback or report an issue.
     * Action: Intent.ACTION_SENDTO
     * URI: mailto:
     */
    fun sendFeedbackEmail(
        context: Context,
        recipientEmail: String = "feedback@weatherandcitytracker.com",
        subject: String = "Weather & City Tracker Feedback"
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipientEmail")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.error_no_email_client),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

