package com.journaldev.androidmvvm.common

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Gharjyot Singh
 */


object BindingUtils {

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getDate(responseDate: String): String {
        var startDateTime: String
        startDateTime= try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US) as DateFormat
            originalFormat.timeZone=TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a") as DateFormat

            val date = originalFormat.parse(responseDate) as Date
             targetFormat.format(date)

        } catch (e: Exception) {
            e.printStackTrace()
            "00-00-0000 00:00 AM"
        }

        return startDateTime.split(" ")[0]

    }

    @JvmStatic
    fun getTime(responseDate: String): String {

        var startDateTime: String

        startDateTime = try {

            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            originalFormat.timeZone=TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")

            val date = originalFormat.parse(responseDate)
            targetFormat.format(date)


        } catch (e: Exception) {
            e.printStackTrace()
            "00-00-0000 00:00 AM"
        }


        startDateTime = startDateTime.replace("a. m", "a.m")
        startDateTime = startDateTime.replace("p. m", "p.m")
        val starttime = startDateTime.split(" ")[1]
        return starttime
    }

//    @JvmStatic
//    fun getDateTime(responseDate: String): String {
//
//        var startDateTime: String;
//
//        try {
//
//
//            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
//            originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
//            val targetFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
//
//            val date = originalFormat.parse(responseDate)
//            startDateTime = targetFormat.format(date)
//
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            startDateTime = "00-00-0000 00:00 AM"
//        }
//
//
//        startDateTime = startDateTime.replace("a. m", "a.m")
//        startDateTime = startDateTime.replace("p. m", "p.m")
//
//
//        val startDate = startDateTime.split(" ")[0]
//
//        val starttime = startDateTime.split(" ")[1]
//        val returnString=startDate + " " + starttime
//        return returnString
//    }
}