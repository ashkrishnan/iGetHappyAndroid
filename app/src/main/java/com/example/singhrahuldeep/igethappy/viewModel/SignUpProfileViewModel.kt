package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.ForgotPasswordResponse
import com.example.singhrahuldeep.igethappy.validations.SignUpProfileValidation
/**
 * Created by Gharjyot Singh
 */

class SignUpProfileViewModel() : ViewModel() {
    var data: MutableLiveData<ForgotPasswordResponse>? = null
    var forgotData: MutableLiveData<ForgotPasswordResponse>? = null
    var signUpProfileCallbacks: CallBackResult.SignUpProfileCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(signUpProfileCallbacks: CallBackResult.SignUpProfileCallbacks) {
        this.signUpProfileCallbacks = signUpProfileCallbacks

    }

    val loading: LiveData<Boolean>
        get() = isLoading

    fun onContinueClicked(vararg args: String) {
        val firstName = args[0]
        val lastName = args[1]
        val nickName = args[2]
        val signUpProfileValidation = SignUpProfileValidation(signUpProfileCallbacks)
        signUpProfileValidation.checkForgotValidation(firstName, lastName, nickName)
    }

    fun getData(): LiveData<ForgotPasswordResponse> {

        if (forgotData == null) {
            forgotData = MutableLiveData()
        }

        return forgotData!!
    }


}