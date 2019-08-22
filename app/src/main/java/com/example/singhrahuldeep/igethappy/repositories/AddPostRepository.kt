package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.AddPostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class AddPostRepository {

    var data: MutableLiveData<AddPostResponse.Data>? = null
    var dataResponse: AddPostResponse.Data? = null

    fun initRepo() {
        data = MutableLiveData()
    }

    fun addTextPost(
        addPostInput: HashMap<String, RequestBody>,
        allData: MutableLiveData<AddPostResponse.Data>,
        mIsUpdating: MutableLiveData<Boolean>
    ) {
        val call = GetRestAdapter.getRestAdapter(false).addPost(addPostInput)
        call.enqueue(object : Callback<AddPostResponse> {
            override fun onResponse(
                call: Call<AddPostResponse>,
                response: Response<AddPostResponse>?
            ) {
                if (response?.body()?.status == "200") {
                    dataResponse = response.body().data
                } else {
                    dataResponse = null
                }

                allData.value = dataResponse
                mIsUpdating.value = false
            }

            override fun onFailure(call: Call<AddPostResponse>, t: Throwable) {
                allData.value = null
                mIsUpdating.value = false
            }
        })
    }


    fun addPost(
        addPostInput: HashMap<String, RequestBody>,
        allData: MutableLiveData<AddPostResponse.Data>,
        mIsUpdating: MutableLiveData<Boolean>,
        prepareFilePart: MultipartBody.Part
    ) {
        val call = GetRestAdapter.getRestAdapter(false).addPost(addPostInput, prepareFilePart)

        call.enqueue(object : Callback<AddPostResponse> {
            override fun onResponse(
                call: Call<AddPostResponse>,
                response: Response<AddPostResponse>?
            ) {
                if (response?.body()?.status == "200") {
                    dataResponse = response.body().data
                } else {
                    dataResponse = null
                }

                allData.value = dataResponse
                mIsUpdating.value = false
            }

            override fun onFailure(call: Call<AddPostResponse>, t: Throwable) {
                allData.value = null
                mIsUpdating.value = false
            }
        })
    }
}