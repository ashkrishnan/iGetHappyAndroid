package com.example.singhrahuldeep.igethappy.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.GetPostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Gharjyot Singh
 */


class GetPostRepository() {

    var mArrayList: ArrayList<GetPostResponse.Data>? = null
    var data: MutableLiveData<ArrayList<GetPostResponse.Data>>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null

    fun initRepo() {
        this.callBackResult = callBackResult
        data = MutableLiveData()
    }

    fun fetchPosts(mHashMap: HashMap<String, String>): MutableLiveData<ArrayList<GetPostResponse.Data>> {
        val call = GetRestAdapter.getRestAdapter(false).fetchPost(mHashMap)
        call.enqueue(object : Callback<GetPostResponse> {
            override fun onResponse(
                call: Call<GetPostResponse>,
                response: Response<GetPostResponse>?
            ) {
                Log.d("ResponseUrl", " url: ${response!!.raw().request().url()}")
                if (response?.body()?.status == "200") {

                    mArrayList = response.body().data
                } else
                    mArrayList = null

                data!!.value = mArrayList

            }

            override fun onFailure(call: Call<GetPostResponse>, t: Throwable) {
                data!!.value = null
            }
        })

        return data!!
    }
}