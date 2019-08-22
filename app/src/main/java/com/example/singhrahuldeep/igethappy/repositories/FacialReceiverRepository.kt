package com.example.singhrahuldeep.igethappy.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.FacialResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class FacialReceiverRepository {

    var data: MutableLiveData<FacialResponse>? = null
    var dataResponse: FacialResponse? = null

    fun initRepo() {
        data = MutableLiveData()
    }

    fun trackMood(
        allData: MutableLiveData<FacialResponse>,
        mIsUpdating: MutableLiveData<Boolean>,
        prepareFilePart: MultipartBody.Part
    ) {
        val call = GetRestAdapter.getFacialRestAdapter(false).facialRecognition(prepareFilePart)
        call.enqueue(object : Callback<FacialResponse> {
            override fun onResponse(
                call: Call<FacialResponse>,
                response: Response<FacialResponse>?
            ) {
                Log.d("", "" + response)
                allData.value = response!!.body()
                mIsUpdating.value = false
            }

            override fun onFailure(call: Call<FacialResponse>, t: Throwable) {
                allData.value = null
                mIsUpdating.value = false
            }
        })
    }
}