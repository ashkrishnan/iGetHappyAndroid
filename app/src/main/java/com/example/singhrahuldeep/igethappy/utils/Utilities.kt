package com.example.singhrahuldeep.igethappy.utils


import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.igethappy.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.math.BigDecimal
import java.util.regex.Matcher
import java.util.regex.Pattern
/**
 * Created by Gharjyot Singh
 */



class Utilities {

    // automatic turn off the gps
    fun turnGPSOff(ctx: Context) {
        val provider = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        if (provider.contains("gps")) { //if gps is enabled
            val poke = Intent()
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            ctx.sendBroadcast(poke)
        }
    }


    fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, string
        )
    }

    fun prepareFilePart(partName: String, fileUri: File): MultipartBody.Part {
        val requestFile = RequestBody.create(
            MediaType.parse("image/*"),
            fileUri
        )
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
    }

    companion object {
        var loading: ProgressDialog? = null

       /* fun showProgress(context: Context) {
            loading = ProgressDialog(context)
            loading!!.setCancelable(true)
            loading!!.setMessage("Please wait...")
            loading!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            loading!!.show()
        }*/

        fun dismissProgress() {
            if (loading != null) {
                try {
                    loading!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        var isDialogShowing = false
        var distressDialog: Dialog? = null
        var distressTimer: CountDownTimer? = null
        var alertDialog: Dialog? = null


        var ratingReviews = "Rating & Reviews"
        var timingSlots: ArrayList<String>? = null

        /**
         * This method is used to round off a value to one decimal place.
         *
         * @param doubleValue_str - Value to round-off.
         * @return Rounded-off value in double.
         */
        fun getRoundValue(doubleValue_str: String): Double {
            var returnValue: Double
            try {
                var bigDecimal = BigDecimal(doubleValue_str)
                bigDecimal = bigDecimal.divide(
                    BigDecimal("1"), 1,
                    BigDecimal.ROUND_HALF_UP
                )
                returnValue = java.lang.Double.parseDouble(bigDecimal.toString())
            } catch (e: Exception) {
                returnValue = 0.0
            }

            return returnValue
        }

        fun dismissDialog(dialog: Dialog?) {

            try {
                if (dialog != null && dialog.isShowing) {
                    dialog.dismiss()
                }
            } catch (e: Exception) {

            }

        }

        /**
         * This method is used to round off a value to one decimal place.
         *
         * @param doubleValue_str - Value to round-off.
         * @return Rounded-off value in double.
         */
        fun getRoundValueOfTwoDigit(doubleValue_str: String): Float {
            var returnValue: Float
            try {
                var bigDecimal = BigDecimal(doubleValue_str)
                bigDecimal = bigDecimal.divide(
                    BigDecimal("1"), 2,
                    BigDecimal.ROUND_HALF_UP
                )
                returnValue = java.lang.Float.parseFloat(bigDecimal.toString())
            } catch (e: Exception) {
                returnValue = 0.0f
            }

            return returnValue
        }


        @SuppressLint("MissingPermission")
                /**
                 * This method checks if the device has an active internet
                 * connection or not.
                 *
                 * @param activity - Activity where this dialog will be shown
                 * @return Returns true if there is internet connectivity
                 */
        fun checkInternet(activity: Context): Boolean {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return if (netInfo != null && netInfo.isConnectedOrConnecting) {
                true
            } else if (netInfo != null && (netInfo.state == NetworkInfo.State.DISCONNECTED || netInfo.state == NetworkInfo.State.DISCONNECTING || netInfo.state == NetworkInfo.State.SUSPENDED || netInfo.state == NetworkInfo.State.UNKNOWN)) {
                false
            } else {
                false
            }
        }


        /**
         * Display a small toast message to the user.
         *
         * @param activity - Activity where this toast will be shown
         * @param message  - Message to be shown when short toast is shown.
         */
        fun showSmallToast(activity: AppCompatActivity, message: String) {
            Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        /**
         * Display a small toast message to the user.
         *
         * @param mContext - Activity where this toast will be shown
         * @param message  - Message to be shown when long toast is shown.
         */
        fun showlongToast(mContext: Context, message: String) {
            Toast.makeText(mContext.applicationContext, message, Toast.LENGTH_SHORT).show()
        }


        fun isEmptyOrWhitespace(s: String): Boolean {

            for (i in 0 until s.length) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                if (Character.isWhitespace(s[i])) {
                    return true
                }
            }
            return false
        }


        /**
         * Display an login dialog to the user.
         *
         * @param context - Context of the activity
         */
        fun showNoInternetMsg(context: Context) {
            val b = AlertDialog.Builder(context)
            val a = b.create()
            // a.setIcon(R.drawable.camera_icon);
            a.setTitle(context.resources.getString(R.string.app_name))
            //TODO    a.setMessage(context.getResources().getString(R.string.no_internet_msg));
            a.setButton(0, "OK", null) { arg0, arg1 -> }
            try {
                a.show()
            } catch (e: Exception) {
            }

        }

        /**
         * @param context - Context of the activity
         * @param title   - Title which will be shown on top of Alert Dialog
         * @param message - Message to be shown when Alert dialog is shown
         */
        fun showMessage(context: Context, title: String, message: String) {
            val b = AlertDialog.Builder(context)
            val a = b.create()
            // a.setIcon(R.drawable.camera_icon);
            a.setTitle(title)
            a.setMessage(message)
            a.setButton(0, "OK", null) { arg0, arg1 -> }
            try {
                a.show()
            } catch (e: Exception) {
            }

        }


        /**
         * Verify an email address.
         *
         * @param email - Email Address that need to be verified
         * @return Returns true if email is valid.
         */
        fun isValidEmail(email: String): Boolean {
            val eMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(eMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            return matcher.matches()

        }

        fun isValidMobile(phone: String): Boolean {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }

        private val PASSWORD_PATTERN = "/^(?=.*\\d)(?=.*[A-Z])([@\$%&#])[0-9a-zA-Z]{4,}\$/"


        fun isPasswordValid(password: String): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
            val pASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
            pattern = Pattern.compile(pASSWORD_REGEX)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

        //public static GoogleCloudMessaging gcm;
        var regid: String? = null

        fun showMessage(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            //        AlertDialog.Builder b = new AlertDialog.Builder(context);
            //        AlertDialog a = b.create();
            //        // a.setIcon(R.drawable.camera_icon);
            //        //TODO   a.setTitle(context.getString(R.string.message_title));
            //        a.setMessage(message);
            //        a.setTitle("Ten");
            //        a.setButton("OK", new DialogInterface.OnClickListener() {
            //            public void onClick(DialogInterface arg0, int arg1) {
            //
            //            }
            //        });
            //
            //        try {
            //            a.show();
            //        } catch (Exception e) {
            //        }

        }

        fun getScreenOrientation(activity: AppCompatActivity): Int {

            var sCREEN_ORIENTATION = 0
            val rotation = activity.windowManager.defaultDisplay.rotation
            when (rotation) {
                Surface.ROTATION_0 -> sCREEN_ORIENTATION = 0
                Surface.ROTATION_90 -> sCREEN_ORIENTATION = 1
                Surface.ROTATION_180 -> sCREEN_ORIENTATION = 0
                Surface.ROTATION_270 -> sCREEN_ORIENTATION = 3
            }

            return sCREEN_ORIENTATION
        }


        fun hideKeypad(v: View) {
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        /**
         * This method is used to  hideKeyboard Automatically
         *
         * @param activity :context of activity
         */
        fun hideSoftKeyboard(activity: AppCompatActivity) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }


        /*fun vibratePhone(activity: Activity) {
            val v = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Vibrate for 200 milliseconds
            v.vibrate(2000)
        }*/

        fun showDialog(dialog: Dialog) {
            try {
                dialog.show()
            } catch (e: Exception) {
                Log.e("dialog Show exception--", e.toString() + "")
            }

        }

        fun turnGPSOn(ctx: Context) {
            try {
                val intent = Intent("android.location.GPS_ENABLED_CHANGE")
                intent.putExtra("enabled", true)
                ctx.sendBroadcast(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val provider = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            if (!provider.contains("gps")) { //if gps is disabled
                val poke = Intent()
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
                poke.data = Uri.parse("3")
                ctx.sendBroadcast(poke)
            }
        }

        fun showKeyboard(context: Context) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        fun hideKeyboard(context: Context) {
            try {
                (context as AppCompatActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                if (context.currentFocus != null && context.currentFocus!!.windowToken != null) {
                    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        context.currentFocus!!.windowToken,
                        0
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun createPartFromString(string: String): RequestBody {
            return RequestBody.create(
                MultipartBody.FORM, string
            )
        }


        fun prepareFilePart(partName: String, fileUri: File): MultipartBody.Part {
            val requestFile = RequestBody.create(MediaType.parse("image/*"), fileUri)
            return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
        }

        fun prepareAudioFilePart(partName: String, fileUri: File): MultipartBody.Part {
            val requestFile = RequestBody.create(
                MediaType.parse("audio/wave"),
                fileUri
            )
            return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
        }

        fun prepareVideoFilePart(partName: String, fileUri: File): MultipartBody.Part {
            val requestFile = RequestBody.create(
                MediaType.parse("video/mp4"),
                fileUri
            )
            return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
        }

    }
}
