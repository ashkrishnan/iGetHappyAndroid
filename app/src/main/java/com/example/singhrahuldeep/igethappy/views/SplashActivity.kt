package com.example.singhrahuldeep.igethappy.views

import android.content.Intent
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.views.auth.LoginActivity
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.example.singhrahuldeep.igethappy.views.dashboard.LandingPageActivity

/**
 * Created by Gharjyot Singh
 */


class SplashActivity : BaseActivity() {

    private var timerThread: Thread? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initViews() {

        enableLogs()

        timerThread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)

                    val prefs = SharedPrefsHelper(this@SplashActivity)
                    val userId = prefs.get(PreferenceKeys.PREF_USER_ID, "")

                    if (userId.length != 0) {
                        val intent = Intent(this@SplashActivity, LandingPageActivity::class.java)
                        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        timerThread!!.start()

    }

    override fun dispose() {

    }


}
