package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel

/**
 * Created by Gharjyot Singh
 */

class WelcomeViewModel(): ViewModel() {

    var dataList: MutableLiveData<LoginResponseModel>? = null
    var welcomeCallbacks: CallBackResult.WelcomeCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(welcomeCallbacks: CallBackResult.WelcomeCallbacks) {
        this.welcomeCallbacks = welcomeCallbacks

    }

    val list: LiveData<LoginResponseModel>?
        get() = dataList


    fun onBeginClicked() {
        welcomeCallbacks!!.onBeginClicked()
    }

}