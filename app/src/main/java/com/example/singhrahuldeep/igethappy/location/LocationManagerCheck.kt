package com.example.singhrahuldeep.igethappy.location

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.provider.Settings
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.igethappy.R
/**
 * Created by Gharjyot Singh
 */



class LocationManagerCheck(context: Context) {
    internal var locationManager: LocationManager
    var isLocationServiceAvailable: Boolean? = false
        internal set
    var providerType = 0
        internal set

    init {
        locationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsIsEnabled = locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkIsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (networkIsEnabled == true && gpsIsEnabled == true) {
            isLocationServiceAvailable = true
            providerType = 1

        } else if (networkIsEnabled != true && gpsIsEnabled == true) {
            isLocationServiceAvailable = true
            providerType = 2

        } else if (networkIsEnabled == true && gpsIsEnabled != true) {
            isLocationServiceAvailable = true
            providerType = 1
        }

    }

    fun createLocationServiceError(activityObj: AppCompatActivity) {

        // show alert dialog if Internet is not connected
        val builder = AlertDialog.Builder(activityObj)

        builder.setMessage(
            "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings"
        )
            .setTitle("GPS Setting")
            .setCancelable(false)
            .setPositiveButton(
                "Settings"
            ) { dialog, id ->
                val intent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                activityObj.startActivity(intent)
                alert!!.dismiss()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, id -> alert!!.dismiss() }
        alert = builder.create()
        alert!!.show()
    }

    companion object {
        internal var alert: AlertDialog? = null
    }


    fun showGpsDialog(activityObj: AppCompatActivity) {

        val dialog = Dialog(activityObj)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_gps)
        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //post_title
        val tvMessage = dialog.findViewById<TextView>(R.id.tv_message) as TextView
        tvMessage.text =
            "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings"


        val tvYes = dialog.findViewById<TextView>(R.id.tv_yes) as TextView
        val tvNo = dialog.findViewById<TextView>(R.id.tv_no) as TextView


        tvYes.setOnClickListener { view ->
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            activityObj.startActivity(intent)
            dialog.dismiss()

        }

        tvNo.setOnClickListener { view ->

            dialog.dismiss()

        }

        dialog.show()
    }


}
