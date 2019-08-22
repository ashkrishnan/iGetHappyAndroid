package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class LoginEmailRepository() {


    var data: MutableLiveData<LoginResponseModel.Data>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null
    var loginEmailCallback: CallBackResult.LoginEmailCallbacks? = null
    var responseData: LoginResponseModel? = null

    fun initRepo(loginEmailCallback: CallBackResult.LoginEmailCallbacks) {
        this.callBackResult = callBackResult
        this.loginEmailCallback = loginEmailCallback
        data = MutableLiveData()
    }

    fun loginUser(loginInput: HashMap<String, RequestBody>, data: MutableLiveData<LoginResponseModel>) {
        val call = GetRestAdapter.getRestAdapter(false).loginForm(loginInput)
        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>?) {
                if (response?.body()?.status == "200") {
                    responseData = response.body()
                } else {
                    responseData = response?.body()
                }

                data!!.value = responseData
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                loginEmailCallback?.onResponseError(call, t)
            }
        })
    }
}