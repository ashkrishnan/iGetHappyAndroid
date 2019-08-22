package com.journaldev.androidmvvm.common

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.content.ContextCompat
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.App
/**
 * Created by Gharjyot Singh
 */


object UtilsFunctions {

    @JvmStatic
    fun showToastError(message: String) {

        val toast = Toast.makeText(App.instance, message, LENGTH_LONG) as Toast
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        val view = toast.view
        val group = toast.view as ViewGroup
        val messageTextView = group.getChildAt(0) as TextView
        messageTextView.textSize = 15.0f
        messageTextView.gravity = Gravity.CENTER
        view.setBackgroundColor(ContextCompat.getColor(App.instance, R.color.red))
        toast.show()
    }

    @JvmStatic
    fun showToastSuccess(message: String) {


        val toast = Toast.makeText(App.instance, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        val view = toast.view
        val group = toast.view as ViewGroup
        val messageTextView = group.getChildAt(0) as TextView
        messageTextView.textSize = 15.0f
        messageTextView.gravity = Gravity.CENTER
        view.setBackgroundColor(ContextCompat.getColor(App.instance, R.color.green_color))
        toast.show()


    }

    @JvmStatic
    fun showToastWarning(message: String) {
        val toast = Toast.makeText(App.instance, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        val view = toast.view
        val group = toast.view as ViewGroup
        val messageTextView = group.getChildAt(0) as TextView
        messageTextView.textSize = 16.0f
        messageTextView.gravity = Gravity.CENTER
        view.setBackgroundColor(ContextCompat.getColor(App.instance, R.color.transparentBlack))
        toast.show()
    }


}
