package com.example.singhrahuldeep.igethappy.views

import android.os.Bundle
import android.util.Log
import com.example.singhrahuldeep.igethappy.interfaces.FacebookInterface
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*

/**
 * Created by Gharjyot Singh
 */

class FacebookLogin(callbackManager: CallbackManager?, loginButton: LoginButton, facebookInterface: FacebookInterface) {

    private var personPhotoUrl: String? = null
    private var email: String? = null
    private val tAG = "FacebookLogin"


    init {
        loginButton.setReadPermissions(
            Arrays.asList("email", "public_profile")
        )
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                println("loginResult $loginResult")
                setFacebookData(loginResult, facebookInterface)
            }

            override fun onCancel() {

                println("Cancel")
            }

            override fun onError(exception: FacebookException) {
                println("Facebook Exception$exception")
            }
        })
    }

    private fun setFacebookData(loginResult: LoginResult, facebookInterface: FacebookInterface) {
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
            // Application code:
            if (response != null) {
                try {
                    Log.d(tAG, "access_token" + loginResult.accessToken)
                    Log.d("Response", response.toString())
                    if (response.jsonObject.has("email")) {
                        email = response.jsonObject.getString("email")
                    }
                    var id: String? = null
                    var firstName = ""
                    var lastName = ""
                    val profile = Profile.getCurrentProfile()
                    if (profile != null) {
                        id = profile.id
                        firstName = profile.firstName
                        lastName = profile.lastName
                        personPhotoUrl = Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString()
                    } else if (response != null) {
                        id = response.jsonObject.getString("id")
                        firstName = response.jsonObject.getString("first_name")
                        lastName = response.jsonObject.getString("last_name")

                    }
                    if (id != null)
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut()
                        }
                    facebookInterface.facebookLoginSuccess(firstName, email, id, lastName, personPhotoUrl)

                } catch (e: Exception) {
                    Log.d("FB Exception", "excepetion$e")
                }

            } else {
                println("Response Null")
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,email,first_name,last_name")
        request.parameters = parameters
        request.executeAsync()
    }

}

