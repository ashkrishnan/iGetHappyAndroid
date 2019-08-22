package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.LikePostResponseModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class LikePostRepository {

    var data: MutableLiveData<LikePostResponseModel.Data>? = null
    var dataResponse: LikePostResponseModel.Data? = null

    fun initRepo() {
        data = MutableLiveData()
    }

    fun likePost(
        likeInput: HashMap<String, RequestBody>,
        allData: MutableLiveData<LikePostResponseModel.Data>,
        mIsUpdating: MutableLiveData<Boolean>
    ) {
        val call = GetRestAdapter.getRestAdapter(false).likePost(likeInput)
        call.enqueue(object : Callback<LikePostResponseModel> {
            override fun onResponse(
                call: Call<LikePostResponseModel>,
                response: Response<LikePostResponseModel>?
            ) {
                if (response?.body()?.status == "200") {
                    dataResponse = response.body().data
                } else {
                    dataResponse = null
                }

                allData.value = dataResponse
                mIsUpdating.value = false
            }

            override fun onFailure(call: Call<LikePostResponseModel>, t: Throwable) {
                allData.value = null
                mIsUpdating.value = false
            }
        })
    }
}