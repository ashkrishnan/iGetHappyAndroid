package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.model.output.FacialResponse
import com.example.singhrahuldeep.igethappy.repositories.FacialReceiverRepository
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
/**
 * Created by Gharjyot Singh
 */

class FacialViewModel : ViewModel() {

    var dataResponse: MutableLiveData<FacialResponse>? = null
    private var allData: MutableLiveData<FacialResponse>? = null
    var postRepo: FacialReceiverRepository? = null
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val responseMessage = MutableLiveData<String>()

    fun init() {
        postRepo = FacialReceiverRepository()
        postRepo?.initRepo()

        if (dataResponse != null) return
    }


    fun trackMood(path: String) {

        postRepo!!.trackMood(
            allData!!, mIsUpdating,
            prepareFilePart(AppConstants.FILE, File(path))
        )

    }


    fun getData(): LiveData<FacialResponse> {

        if (allData == null) {
            allData = MutableLiveData()
        }
        return allData!!

    }

    val message: LiveData<String>
        get() = responseMessage

    val loading: LiveData<Boolean>
        get() = mIsUpdating


    fun prepareFilePart(partName: String, fileUri: File): MultipartBody.Part {
        val requestFile = RequestBody.create(
            MediaType.parse("image/png"),
            fileUri
        )
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
    }

    fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, string
        )
    }

}