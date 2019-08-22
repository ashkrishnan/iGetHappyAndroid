package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.LanguageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class GetLanguageRepository() {

    var dataResponse: Response<LanguageResponse>? = null
    var data: MutableLiveData<LanguageResponse>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null

    fun initRepo() {
        this.callBackResult = callBackResult
        data = MutableLiveData()
    }

    fun fetchLanguages(locale: String): MutableLiveData<LanguageResponse> {
        val call = GetRestAdapter.getRestAdapter(false).fetchLanguages(locale)
        call.enqueue(object : Callback<LanguageResponse> {
            override fun onResponse(
                call: Call<LanguageResponse>,
                response: Response<LanguageResponse>?
            ) {

                if (response!!.body()?.status!!.equals("200")) {
                    dataResponse = response
                } else
                    dataResponse = null

                data!!.value = dataResponse!!.body()

            }

            override fun onFailure(call: Call<LanguageResponse>, t: Throwable) {
                data!!.value = null
            }
        })

        return data!!
    }
}