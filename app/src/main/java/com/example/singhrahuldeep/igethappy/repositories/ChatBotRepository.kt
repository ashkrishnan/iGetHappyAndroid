package com.example.singhrahuldeep.igethappy.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.api.GetRestAdapter
import com.example.singhrahuldeep.igethappy.model.output.ChatBotResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */


class ChatBotRepository() {

    var dataList: MutableLiveData<ChatBotResponseModel>? = null

    var data: MutableLiveData<ChatBotResponseModel>? = null
    var callBackResult: CallBackResult.VerifyNumberCallbacks? = null
    var chatBotCallback: CallBackResult.ChatBotCallbacks? = null

    fun initRepo(chatBotCallback: CallBackResult.ChatBotCallbacks) {
        this.callBackResult = callBackResult
        this.chatBotCallback = chatBotCallback
        data = MutableLiveData()
    }

    val list: LiveData<ChatBotResponseModel>?
        get() = dataList

    fun chatBot() {
        val call = GetRestAdapter.getChatRestAdapter(false).chatBot()
        call.enqueue(object : Callback<ChatBotResponseModel> {
            override fun onResponse(
                call: Call<ChatBotResponseModel>,
                response: Response<ChatBotResponseModel>?
            ) {
                if (response?.body()?.message!!.length != 0) {
                    chatBotCallback?.onResponseSuccess(response)
                } else
                    chatBotCallback?.onResponseSuccessError(
                        response.body()?.message
                    )

            }

            override fun onFailure(call: Call<ChatBotResponseModel>, t: Throwable) {
                chatBotCallback?.onResponseError(call, t)
            }
        })
    }
}