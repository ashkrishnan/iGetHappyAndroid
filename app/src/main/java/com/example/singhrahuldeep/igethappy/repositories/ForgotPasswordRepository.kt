package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.input.ForgotPasswordInput
import com.example.singhrahuldeep.igethappy.model.output.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class ForgotPasswordRepository() {

    var data: MutableLiveData<ForgotPasswordResponse>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null
    var forgotPassCallback: CallBackResult.ForgotCallbacks? = null
    var responseData: ForgotPasswordResponse? = null

    fun initRepo(forgotPassCallback: CallBackResult.ForgotCallbacks) {
        this.callBackResult = callBackResult
        this.forgotPassCallback = forgotPassCallback
        data = MutableLiveData()
    }

    fun forgotPassword(forgotInput: ForgotPasswordInput, data: MutableLiveData<ForgotPasswordResponse>) {
        val call = GetRestAdapter.getRestAdapter(false).forgotPass(forgotInput)
        call.enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>?) {
                if (response?.body()?.status == "200") {
                    responseData = response.body()
                } else {
                    responseData = response?.body()
                }
                data!!.value = responseData
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                data!!.value = null
            }
        })
    }
}