package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.interfaces.CareReceiverCallback
import com.example.singhrahuldeep.igethappy.model.output.DeleteCareReceiverResponse
import com.example.singhrahuldeep.igethappy.repositories.DeleteCareReceiverRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
/**
 * Created by Gharjyot Singh
 */

class DeleteCareReceiverViewModel : ViewModel() {

    var dataResponse: MutableLiveData<DeleteCareReceiverResponse>? = null
    private var allData: MutableLiveData<DeleteCareReceiverResponse>? = null
    var postRepo: DeleteCareReceiverRepository? = null
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val responseMessage = MutableLiveData<String>()
    var callback: CareReceiverCallback? = null

    fun init(callback: CareReceiverCallback) {
        this.callback = callback
        postRepo = DeleteCareReceiverRepository()
        postRepo?.initRepo()

        if (dataResponse != null) return
    }

    fun deleteCareReceiver(careReceiverId: String) {

        postRepo!!.deleteCareReceiver(careReceiverId, allData!!, mIsUpdating, responseMessage)
    }

    fun getData(): LiveData<DeleteCareReceiverResponse> {

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