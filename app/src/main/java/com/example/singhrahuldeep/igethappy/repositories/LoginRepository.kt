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


class LoginRepository() {
    var data: MutableLiveData<LoginResponseModel>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null
    var loginCallback: CallBackResult.LoginCallbacks? = null

    fun initRepo(loginCallback: CallBackResult.LoginCallbacks) {
        this.callBackResult = callBackResult
        this.loginCallback = loginCallback
        data = MutableLiveData()
    }

    fun checkSocialIdExist(loginInput: HashMap<String, RequestBody>) {
        // var url = "users/isSocialIdExists/" + socialId

        val call = GetRestAdapter.getRestAdapter(false).loginForm(loginInput)
        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>?
            ) {
                if (response?.body()?.status == "200") {
                    loginCallback?.onResponseSuccess(response)
                } else {
                    loginCallback?.onResponseSuccess(response)
                }
                /*loginCallback?.onResponseSuccessError(
                    response?.body()?.message
                )*/
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                loginCallback?.onResponseError(call, t)
            }
        })
    }
}