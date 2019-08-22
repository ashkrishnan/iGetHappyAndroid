package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.model.output.GetPostResponse
import com.example.singhrahuldeep.igethappy.repositories.GetPostRepository
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import java.util.ArrayList
/**
 * Created by Gharjyot Singh
 */

class PostFragmentViewModel: ViewModel(){

    var dataResponse: MutableLiveData<GetPostResponse>? = null
    private var allList: MutableLiveData<ArrayList<GetPostResponse.Data>>? = null
    var chatBotRepo: GetPostRepository? = null
    var isLoading = MutableLiveData<Boolean>()
    private val mIsUpdating = MutableLiveData<Boolean>()


    fun init() {
        chatBotRepo = GetPostRepository()
        chatBotRepo?.initRepo()
        if (dataResponse != null) return
    }

    val loading: LiveData<Boolean>
        get() = mIsUpdating

    fun fetchPost() {
        mIsUpdating.postValue(true)
        val mHashMap = HashMap<String, String>()
        mHashMap.put(AppConstants.USER_ID, AppConstants.APP_USER_ID)
        allList = chatBotRepo!!.fetchPosts(mHashMap)
    }

    val list: LiveData<ArrayList<GetPostResponse.Data>>?
        get() = allList!!


}