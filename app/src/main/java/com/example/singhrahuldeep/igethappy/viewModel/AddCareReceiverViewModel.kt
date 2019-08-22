package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.interfaces.CareReceiverCallback
import com.example.singhrahuldeep.igethappy.model.output.AddCareReceiverResponse
import com.example.singhrahuldeep.igethappy.repositories.AddCareReceiverRepository
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.Utilities
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Gharjyot Singh
 */


class AddCareReceiverViewModel : ViewModel() {

    var dataResponse: MutableLiveData<AddCareReceiverResponse>? = null
    private var allData: MutableLiveData<AddCareReceiverResponse.Data>? = null
    var postRepo: AddCareReceiverRepository? = null
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val responseMessage = MutableLiveData<String>()
    var callback: CareReceiverCallback? = null

    fun init(callback: CareReceiverCallback) {
        this.callback = callback
        postRepo = AddCareReceiverRepository()
        postRepo?.initRepo()

        if (dataResponse != null) return
    }

    fun addCareReceiver() {

        if (callback!!.connectedToInternet()) {
            addData()
        } else
            callback!!.setConnectionError()

    }

    fun addData() {
        if (validationsCorrect()) {
            val mHashMap = HashMap<String, RequestBody>()
            mHashMap.put(AppConstants.CARE_GIVER_ID, createPartFromString(AppConstants.APP_USER_ID))
            mHashMap.put(AppConstants.NAME, createPartFromString(callback!!.getName()))
            mHashMap.put(AppConstants.EMAIL, createPartFromString(callback!!.getEmail()))
            mHashMap.put(AppConstants.PHONE, createPartFromString(callback!!.getPhone()))
            mHashMap.put(AppConstants.IS_MINOR, createPartFromString(callback!!.getMinorStatus()))
            mHashMap.put(AppConstants.RELATIONSHIP, createPartFromString(callback!!.getRelationship()))

            postRepo!!.addCareReceiver(
                mHashMap, allData!!, mIsUpdating, responseMessage,
                prepareFilePart(AppConstants.PROFILE_IMAGE, File(callback!!.getProfileImage()))
            )
        }

    }

    fun validationsCorrect(): Boolean {
        if (!callback!!.getName().trim().isNotEmpty()) {
            callback!!.setValidationError(AppConstants.NAME_EMPTY)
            return false
        } else if (!callback!!.getPhone().trim().isNotEmpty()) {
            callback!!.setValidationError(AppConstants.PHONE_EMPTY)
            return false
        } else if (callback!!.getPhone().trim().length < 10) {
            callback!!.setValidationError(AppConstants.PHONE_LENGTH)
            return false
        } else if (!callback!!.getEmail().trim().isNotEmpty()) {
            callback!!.setValidationError(AppConstants.EMAIL_EMPTY)
            return false
        } else if (!Utilities.isValidEmail(callback!!.getEmail().trim())) {
            callback!!.setValidationError(AppConstants.INVALID_EMAIL)
            return false
        } else if (!callback!!.getMinorStatus().trim().isNotEmpty()) {
            callback!!.setValidationError(AppConstants.MINOR_STATUS)
            return false
        } else if (!callback!!.getRelationship().trim().isNotEmpty()) {
            callback!!.setValidationError(AppConstants.RELATION_NO_SELECTED)
            return false
        }

        return true
    }

    fun getData(): LiveData<AddCareReceiverResponse.Data> {

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