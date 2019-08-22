package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import com.example.singhrahuldeep.igethappy.repositories.LoginRepository
import com.example.singhrahuldeep.igethappy.utils.Utilities
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
/**
 * Created by Gharjyot Singh
 */

class LoginViewModel : ViewModel() {

    var dataList: MutableLiveData<LoginResponseModel>? = null
    var loginRepo: LoginRepository? = null
    var loginCallbacks: CallBackResult.LoginCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(loginCallbacks: CallBackResult.LoginCallbacks) {
        this.loginCallbacks = loginCallbacks
        loginRepo = LoginRepository()
        loginRepo?.initRepo(loginCallbacks)
        if (dataList != null) return

    }

    val list: LiveData<LoginResponseModel>?
        get() = dataList


    fun onContinueWithEmailClicked() {
        loginCallbacks!!.onContinueWithEmailClicked()
    }

    fun doValidateSocialId(socialId: String) {
        isLoading.postValue(true)
        val mHashMap = HashMap<String, RequestBody>()
        mHashMap.put("social_id", Utilities.createPartFromString(socialId))
        loginRepo?.checkSocialIdExist(mHashMap)
    }
}