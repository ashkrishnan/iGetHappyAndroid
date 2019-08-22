package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.EmailExistModelResponse
import com.example.singhrahuldeep.igethappy.repositories.SignUpRepository
import com.example.singhrahuldeep.igethappy.validations.SignUpValidations
/**
 * Created by Gharjyot Singh
 */

class SignUpViewModel() : ViewModel() {

    var data: MutableLiveData<EmailExistModelResponse>? = null
    var signupRepo: SignUpRepository? = null
    var verifyPhoneData: MutableLiveData<EmailExistModelResponse>? = null
    var verifyEmailData: MutableLiveData<EmailExistModelResponse>? = null
    var signupCallbacks: CallBackResult.SignUpCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(signupCallbacks: CallBackResult.SignUpCallbacks) {
        this.signupCallbacks = signupCallbacks
        signupRepo = SignUpRepository()
        signupRepo?.initRepo(signupCallbacks)
    }

    fun verifyEmail(): LiveData<EmailExistModelResponse> {
        if (verifyEmailData == null) {
            verifyEmailData = MutableLiveData()
        }
        return verifyEmailData!!
    }

    fun verifyPhone(): LiveData<EmailExistModelResponse> {
        if (verifyPhoneData == null) {
            verifyPhoneData = MutableLiveData()
        }
        return verifyPhoneData!!
    }

    fun onSignUpClicked(email: String, phone: String, pass: String) {
        val loginValidations = SignUpValidations(signupCallbacks)
        loginValidations.checksignUpValidation(email,phone, pass)
    }

    fun checkEmailExist(email: String) {
        isLoading.postValue(true)
        signupRepo!!.checkEmailExist(email, verifyEmailData!!)
    }

    fun checkPhoneExist(email: String) {
        isLoading.postValue(true)
        signupRepo!!.checkPhoneExist(email, verifyPhoneData!!)
    }

}