package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.EmailExistModelResponse
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class SignUpRepository() {

    var data: MutableLiveData<LoginResponseModel>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null
    var signupCallback: CallBackResult.SignUpCallbacks? = null

    fun initRepo(signupCallback: CallBackResult.SignUpCallbacks) {
        this.callBackResult = callBackResult
        this.signupCallback = signupCallback
        data = MutableLiveData()
    }

    fun checkEmailExist(email: String, verifyEmailData: MutableLiveData<EmailExistModelResponse>?) {
        val url = "users/isEmailExists/" + email
        val call = GetRestAdapter.getRestAdapter(false).checkEmailExist(url)
        call.enqueue(object : Callback<EmailExistModelResponse> {
            override fun onResponse(call: Call<EmailExistModelResponse>, response: Response<EmailExistModelResponse>?) {
                if (response != null) {
                    if (response.body().status == "200") {
                        verifyEmailData!!.value = response.body()
                    } else {
                        verifyEmailData!!.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<EmailExistModelResponse>, t: Throwable) {
                // loginCallback?.onResponseError(call, t)
            }
        })
    }

    fun checkPhoneExist(phone: String, verifyPhoneData: MutableLiveData<EmailExistModelResponse>?) {
        val url = "users/isPhoneExists/" + phone
        val call = GetRestAdapter.getRestAdapter(false).checkEmailExist(url)
        call.enqueue(object : Callback<EmailExistModelResponse> {
            override fun onResponse(call: Call<EmailExistModelResponse>, response: Response<EmailExistModelResponse>?) {
                if (response != null) {
                    if (response.body().status == "200") {
                        verifyPhoneData!!.value = response.body()
                    } else {
                        verifyPhoneData!!.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<EmailExistModelResponse>, t: Throwable) {
                // loginCallback?.onResponseError(call, t)
            }
        })
    }

}