package com.example.singhrahuldeep.igethappy.validations

import android.text.TextUtils
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.utils.Utilities
/**
 * Created by Gharjyot Singh
 */


class ForgotPassValidation(
    val ForgotCallbacks: CallBackResult.ForgotCallbacks?
) {
    fun checkForgotValidation(vararg args: String) {
        val email = args[0]

        if (TextUtils.isEmpty(email)) {
            ForgotCallbacks?.onFieldEmpty()
        } else if (Utilities.isValidEmail(email)) {
            ForgotCallbacks?.onValidEmail(email)
        } else if (Utilities.isValidMobile(email)) {
            ForgotCallbacks?.onValidPhone(email)
        } else {
            ForgotCallbacks?.onInValidPhoneEmail()
        }
    }
}