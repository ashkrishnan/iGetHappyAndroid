package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.DeleteCareReceiverResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class DeleteCareReceiverRepository {

    var data: MutableLiveData<DeleteCareReceiverResponse>? = null
    var dataResponse: DeleteCareReceiverResponse? = null

    fun initRepo() {
        data = MutableLiveData()
    }

    fun deleteCareReceiver(
        input: String,
        allData: MutableLiveData<DeleteCareReceiverResponse>,
        mIsUpdating: MutableLiveData<Boolean>, messageResponse: MutableLiveData<String>
    ) {
        val call = GetRestAdapter.getRestAdapter(false).deleteCareReceiver(input)
        call.enqueue(object : Callback<DeleteCareReceiverResponse> {
            override fun onResponse(
                call: Call<DeleteCareReceiverResponse>,
                response: Response<DeleteCareReceiverResponse>?
            ) {
                if (response?.body()?.status == "200") {
                    dataResponse = response.body()
                    allData.value = dataResponse
                } else if (response?.body()?.status == "204") {
                    messageResponse.value = response.body().message
                } else {
                    dataResponse = null
                }


                mIsUpdating.value = false
            }

            override fun onFailure(call: Call<DeleteCareReceiverResponse>, t: Throwable) {
                allData.value = null
                mIsUpdating.value = false
            }
        })
    }
}