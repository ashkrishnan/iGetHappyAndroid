package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.input.ForgotPasswordInput
import com.example.singhrahuldeep.igethappy.model.output.ForgotPasswordResponse
import com.example.singhrahuldeep.igethappy.repositories.ForgotPasswordRepository
import com.example.singhrahuldeep.igethappy.validations.ForgotPassValidation
/**
 * Created by Gharjyot Singh
 */

class ForgotPasswordViewModel() : ViewModel() {

    var data: MutableLiveData<ForgotPasswordResponse>? = null
    var forgotData: MutableLiveData<ForgotPasswordResponse>? = null
    var forgotPassRepo: ForgotPasswordRepository? = null
    var forgotCallbacks: CallBackResult.ForgotCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(forgotCallbacks: CallBackResult.ForgotCallbacks) {
        this.forgotCallbacks = forgotCallbacks
        forgotPassRepo = ForgotPasswordRepository()
        forgotPassRepo?.initRepo(forgotCallbacks)
    }

    val loading: LiveData<Boolean>
        get() = isLoading

    fun onForgotClicked(email: String) {
        val forgotValidation = ForgotPassValidation(forgotCallbacks)
        forgotValidation.checkForgotValidation(email)
    }

    fun onBackClicked() {
        forgotCallbacks!!.onBackClicked()
    }

    fun hitForgotEmail(email: String) {
        isLoading.postValue(true)
        val forgotInput = ForgotPasswordInput()
        forgotInput.email = email
        forgotPassRepo!!.forgotPassword(forgotInput, forgotData!!)
    }

    fun hitForgotPhone(phone: String) {
        isLoading.postValue(true)
        val forgotInput = ForgotPasswordInput()
        forgotInput.phone = phone
        forgotPassRepo!!.forgotPassword(forgotInput, forgotData!!)
    }

    fun getData(): LiveData<ForgotPasswordResponse> {

        if (forgotData == null) {
            forgotData = MutableLiveData()
        }

        return forgotData!!
    }

}