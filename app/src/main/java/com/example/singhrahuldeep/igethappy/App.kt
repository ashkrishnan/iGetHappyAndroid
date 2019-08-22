package com.example.singhrahuldeep.igethappy

import android.app.Application
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.example.singhrahuldeep.igethappy.di.component.ApplicationComponent
import com.facebook.FacebookSdk
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by Gharjyot Singh
 */


class App : Application() {
    private var app: App? = null
    private var mApplicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        this.app = this
        FacebookSdk.sdkInitialize(getApplicationContext());
        keyHash();
    }

    fun getComponent(): ApplicationComponent? {
        return this.mApplicationComponent
    }


    private fun keyHash() {
        try {
            val info = packageManager.getPackageInfo(
                "com.android.igethappy",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash : ", Base64.encodeToString(md.digest(), Base64.DEFAULT))

            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * @return ApplicationController singleton instance
         */
        @get:Synchronized
        lateinit var instance: App
    }


}