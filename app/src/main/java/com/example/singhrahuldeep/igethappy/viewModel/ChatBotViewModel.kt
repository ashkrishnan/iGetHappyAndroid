package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.ChatBotResponseModel
import com.example.singhrahuldeep.igethappy.repositories.ChatBotRepository
/**
 * Created by Gharjyot Singh
 */

class ChatBotViewModel : ViewModel() {

    var dataResponse: MutableLiveData<ChatBotResponseModel>? = null

    var chatBotCallbacks: CallBackResult.ChatBotCallbacks? = null
    var chatBotRepo: ChatBotRepository? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(chatBotCallbacks: CallBackResult.ChatBotCallbacks) {
        this.chatBotCallbacks = chatBotCallbacks
        chatBotRepo = ChatBotRepository()
        chatBotRepo?.initRepo(chatBotCallbacks)
        if (dataResponse != null) return
    }

    fun chatBot() {
        isLoading.postValue(true)

        chatBotRepo!!.chatBot()
    }

    val list: LiveData<ChatBotResponseModel>?
        get() = dataResponse


}