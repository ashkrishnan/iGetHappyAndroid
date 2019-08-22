package com.example.singhrahuldeep.igethappy.validations

import android.text.TextUtils
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.input.LoginInputModel
import com.example.singhrahuldeep.igethappy.utils.Utilities

/**
 * Created by Gharjyot Singh
 */


class LoginEmailValidation(
    val LoginEmailCallbacks: CallBackResult.LoginEmailCallbacks?
) {
    fun checkLoginValidation(vararg args: String) {
        val email = args[0]
        val password = args[1]

        if (TextUtils.isEmpty(email.trim())) {
            LoginEmailCallbacks?.onPhoneNumberEmailEmptyError()
        } else if (Utilities.isValidEmail(email.trim())) {
            if (TextUtils.isEmpty(password.trim())) {
                LoginEmailCallbacks?.onPasswordEmpty()
            } else if (password.length < 8) {
                LoginEmailCallbacks?.onPasswordInvallidLength()
            } else if (Utilities.isPasswordValid(password)) {
                val loginInput = LoginInputModel()
                loginInput.email = email
                loginInput.password = password
                LoginEmailCallbacks?.validateSuccess(loginInput)
            } else {
                LoginEmailCallbacks?.onPasswordError()
            }
        } else if (Utilities.isValidMobile(email.trim())) {
            if (TextUtils.isEmpty(password.trim())) {
                LoginEmailCallbacks?.onPasswordEmpty()
            } else if (password.length < 8) {
                LoginEmailCallbacks?.onPasswordInvallidLength()
            } else if (Utilities.isPasswordValid(password)) {
                val loginInput = LoginInputModel()
                loginInput.phone = email
                loginInput.password = password
                LoginEmailCallbacks?.validateSuccess(loginInput)
            } else {
                LoginEmailCallbacks?.onPasswordError()
            }
        } else {
            LoginEmailCallbacks?.onPhoneNumberEmailError()
        }
    }
}