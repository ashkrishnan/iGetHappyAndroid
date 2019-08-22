package com.example.singhrahuldeep.igethappy.validations

import android.text.TextUtils
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.utils.Utilities

/**
 * Created by Gharjyot Singh
 */


class SignUpValidations(
    val signUpCallbacks: CallBackResult.SignUpCallbacks?
) {
    fun checksignUpValidation(vararg args: String) {
        val email = args[0]
        val phone = args[1]
        val pass = args[2]
        if (TextUtils.isEmpty(email.trim())) {
            signUpCallbacks?.emailEmpty()
        } else if (TextUtils.isEmpty(phone.trim())) {
            signUpCallbacks?.phoneEmpty()
        } else if (phone.length < 10) {
            signUpCallbacks?.isInValidPhone()
        } else if (TextUtils.isEmpty(pass.trim())) {
            signUpCallbacks?.passEmpty()
        } else if (!Utilities.isValidEmail(email.trim())) {
            signUpCallbacks?.isInValidEmail()
        } else if (!Utilities.isValidMobile(phone.trim())) {
            signUpCallbacks?.isInValidPhone()
        } else if (pass.length < 8) {
            signUpCallbacks?.passLengthInvalid()
        } else if (!Utilities.isPasswordValid(pass)) {
            signUpCallbacks?.isInValidPass()
        } else {
            signUpCallbacks?.validSuccess(email.trim(), phone.trim(), pass.trim())
        }
    }
}