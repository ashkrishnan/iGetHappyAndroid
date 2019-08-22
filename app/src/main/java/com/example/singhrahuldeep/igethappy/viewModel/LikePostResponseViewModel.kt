package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.model.output.LikePostResponseModel
import com.example.singhrahuldeep.igethappy.repositories.LikePostRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Gharjyot Singh
 */


class LikePostResponseViewModel : ViewModel() {

    var dataResponse: MutableLiveData<LikePostResponseModel>? = null
    private var allData: MutableLiveData<LikePostResponseModel.Data>? = null
    var postRepo: LikePostRepository? = null
    private val mIsUpdating = MutableLiveData<Boolean>()

    fun init() {
        postRepo = LikePostRepository()
        postRepo?.initRepo()

        if (dataResponse != null) return
    }

    fun likePost(mHashMap : HashMap<String, RequestBody>){

        postRepo!!.likePost(mHashMap,allData!!,mIsUpdating)
    }

    fun getData(): LiveData<LikePostResponseModel.Data>{

        if (allData == null) {
            allData = MutableLiveData()
        }
        return allData!!

    }

    val loading: LiveData<Boolean>
        get() = mIsUpdating


    fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, string
        )
    }

}