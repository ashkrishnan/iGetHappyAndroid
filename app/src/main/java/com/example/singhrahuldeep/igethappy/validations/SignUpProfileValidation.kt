package com.example.singhrahuldeep.igethappy.validations

import android.text.TextUtils
import com.example.singhrahuldeep.igethappy.CallBackResult
/**
 * Created by Gharjyot Singh
 */


class SignUpProfileValidation(
    val SignUpProfileCallbacks: CallBackResult.SignUpProfileCallbacks?
) {
    fun checkForgotValidation(vararg args: String) {
        val firstName = args[0]
        val lastName = args[1]
        val nickName = args[2]

        if (TextUtils.isEmpty(firstName.trim())) {
            SignUpProfileCallbacks?.onFirstNameEmpty()
        } else if (TextUtils.isEmpty(lastName.trim())) {
            SignUpProfileCallbacks?.onlastNameEmpty()
        } else if (TextUtils.isEmpty(nickName.trim())) {
            SignUpProfileCallbacks?.onNickNameEmpty()
        } else if (firstName.length < 1) {
            SignUpProfileCallbacks?.onInvalidFirstName()
        } else if (lastName.length < 1) {
            SignUpProfileCallbacks?.onInvalidLastName()
        } else if (nickName.length < 1) {
            SignUpProfileCallbacks?.onInvalidNickName()
        } else {
            SignUpProfileCallbacks?.onValidationsSuccess(firstName, lastName, nickName)
        }
    }
}