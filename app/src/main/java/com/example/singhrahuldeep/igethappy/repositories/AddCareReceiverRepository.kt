package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.AddCareReceiverResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class AddCareReceiverRepository {

    var data: MutableLiveData<AddCareReceiverResponse.Data>? = null
    var dataResponse: AddCareReceiverResponse.Data? = null

    fun initRepo() {
        data = MutableLiveData()
    }

    fun addCareReceiver(
        input: HashMap<String, RequestBody>,
        allData: MutableLiveData<AddCareReceiverResponse.Data>,
        mIsUpdating: MutableLiveData<Boolean>, messageResponse: MutableLiveData<String>,
        prepareFilePart: MultipartBody.Part
    ) {
        val call = GetRestAdapter.getRestAdapter(false).addCareReceiver(input, prepareFilePart)
        call.enqueue(object : Callback<AddCareReceiverResponse> {
            override fun onResponse(
                call: Call<AddCareReceiverResponse>,
                response: Response<AddCareReceiverResponse>?
            ) {
                if (response?.body()?.status == "200") {
                    dataResponse = response.body().data
                    allData.value = dataResponse
                } else if (response?.body()?.status == "204") {
                    messageResponse.value = response.body().message
                } else {
                    dataResponse = null
                }


                mIsUpdating.value = false
            }

            override fun onFailure(call: Call<AddCareReceiverResponse>, t: Throwable) {
                allData.value = null
                mIsUpdating.value = false
            }
        })
    }
}