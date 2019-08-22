package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class LanguageSignUpRepository() {
    var data: MutableLiveData<SignUpResponse.Data>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null
    var signUpCallback: CallBackResult.LanguageSignupCallbacks? = null
    var responseData: SignUpResponse? = null

    fun initRepo(signUpCallback: CallBackResult.LanguageSignupCallbacks) {
        this.callBackResult = callBackResult
        this.signUpCallback = signUpCallback
        data = MutableLiveData()
    }

    fun signUpUser(
        loginInput: HashMap<String, RequestBody>,
        data: MutableLiveData<SignUpResponse>,
        prepareFilePart: MultipartBody.Part
    ) {
        val call = GetRestAdapter.getRestAdapter(false).signUp(loginInput, prepareFilePart)
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>?) {
                if (response?.body()?.status == "200") {
                    responseData = response.body()
                } else {
                    responseData = response?.body()
                }
                data.value = responseData
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                //loginEmailCallback?.onResponseError(call, t)
            }
        })
    }

//    fun languageList(
//        loginInput: HashMap<String, RequestBody>,
//        data: MutableLiveData<GetLanguageResponse>,
//        prepareFilePart: MultipartBody.Part
//    ) {
//        val call = GetRestAdapter.getRestAdapter(false).findLanguagesList(loginInput, prepareFilePart)
//        call.enqueue(object : Callback<GetLanguageResponse> {
//            override fun onResponse(call: Call<GetLanguageResponse>, response: Response<GetLanguageResponse>?) {
//                if (response?.body()?.status == "200") {
//                    responseData = response.body()
//                } else {
//                    responseData = response?.body()
//                }
//                data!!.value = responseData
//            }
//
//            override fun onFailure(call: Call<GetLanguageResponse>, t: Throwable) {
//                //loginEmailCallback?.onResponseError(call, t)
//            }
//        })
//    }
}