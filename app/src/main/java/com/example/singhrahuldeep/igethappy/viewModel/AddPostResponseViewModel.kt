package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.interfaces.AddPostCallback
import com.example.singhrahuldeep.igethappy.model.output.AddPostResponse
import com.example.singhrahuldeep.igethappy.repositories.AddPostRepository
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
/**
 * Created by Gharjyot Singh
 */

class AddPostResponseViewModel : ViewModel() {

    var dataResponse: MutableLiveData<AddPostResponse>? = null
    private var allData: MutableLiveData<AddPostResponse.Data>? = null
    var postRepo: AddPostRepository? = null
    private var timerThread: Thread? = null
    private val mIsUpdating = MutableLiveData<Boolean>()
    var isVideoPost = false
    var clicked = false
    var videoFilePath = ""
    var callback: AddPostCallback? = null

    fun init(callback: AddPostCallback) {
        this.callback = callback
        postRepo = AddPostRepository()
        postRepo?.initRepo()

        if (dataResponse != null) return
    }

    fun addPost() {

        if (checkValidations()) {
            mIsUpdating.postValue(true)
            val mHashMap = HashMap<String, RequestBody>()
            mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
            mHashMap.put(AppConstants.DESCRIPTION, createPartFromString(callback!!.getPostText()))
            mHashMap.put(AppConstants.PRIVACY_OPTION, createPartFromString(callback!!.getPrivacyStatus()))
            mHashMap.put(AppConstants.IS_ANONYMOUS, createPartFromString(callback!!.getAnnoymousStatus()))
            mHashMap.put(AppConstants.POST_UPLOAD_TYPE, createPartFromString("TEXT"))
            postRepo!!.addTextPost(mHashMap, allData!!, mIsUpdating)
        } else {
            callback!!.setDescriptionError()
        }
    }

    fun addVideoPost() {
        if (videoFilePath.length != 0) {
            mIsUpdating.postValue(true)
            val mHashMap = HashMap<String, RequestBody>()
            mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
            mHashMap.put(AppConstants.DESCRIPTION, createPartFromString(callback!!.getPostText()))
            mHashMap.put(AppConstants.PRIVACY_OPTION, createPartFromString(callback!!.getPrivacyStatus()))
            mHashMap.put(AppConstants.IS_ANONYMOUS, createPartFromString(callback!!.getAnnoymousStatus()))
            mHashMap.put(AppConstants.POST_UPLOAD_TYPE, createPartFromString("VIDEO"))
            postRepo!!.addPost(
                mHashMap,
                allData!!,
                mIsUpdating,
                prepareFilePart(AppConstants.POST_UPLOAD_FILE, File(videoFilePath))
            )
        } else {
            callback!!.setValidationError(AppConstants.ADD_VIDEO)
        }
    }

    fun addAudioPost() {

        if (videoFilePath.length != 0) {
            mIsUpdating.postValue(true)
            val mHashMap = HashMap<String, RequestBody>()
            mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
            mHashMap.put(AppConstants.DESCRIPTION, createPartFromString(callback!!.getPostText()))
            mHashMap.put(AppConstants.PRIVACY_OPTION, createPartFromString(callback!!.getPrivacyStatus()))
            mHashMap.put(AppConstants.IS_ANONYMOUS, createPartFromString(callback!!.getAnnoymousStatus()))
            mHashMap.put(AppConstants.POST_UPLOAD_TYPE, createPartFromString("AUDIO"))
            postRepo!!.addPost(
                mHashMap,
                allData!!,
                mIsUpdating,
                prepareAudioFilePart(AppConstants.POST_UPLOAD_FILE, File(videoFilePath))
            )
        } else {
            callback!!.setValidationError(AppConstants.ADD_AUDIO)
        }
    }

    fun checkValidations(): Boolean {
        val description = callback!!.getPostText()

        return description.isNotEmpty()

    }

    fun onAddPost() {

        if (!clicked) {
            clicked = true
            timerThread = object : Thread() {
                override fun run() {
                    try {
                        sleep(2000)
                        clicked = false
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            timerThread!!.start()
            if (!isVideoPost) {

                if (callback!!.connectedToInternet())
                    addPost()
                else
                    callback!!.setConnectionError()

            } else
                if (callback!!.connectedToInternet())
                    addVideoPost()
                else
                    callback!!.setConnectionError()

        }
    }

    fun uploadAudioPost() {
        if (!clicked) {
            clicked = true
            timerThread = object : Thread() {
                override fun run() {
                    try {
                        sleep(2000)
                        clicked = false
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            timerThread!!.start()
            if (callback!!.connectedToInternet())
                addAudioPost()
            else
                callback!!.setConnectionError()

        }
    }


    fun getData(): LiveData<AddPostResponse.Data> {

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

    fun prepareAudioFilePart(partName: String, fileUri: File): MultipartBody.Part {
        val requestFile = RequestBody.create(
            MediaType.parse("audio/wave"),
            fileUri
        )
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
    }

    fun prepareFilePart(partName: String, fileUri: File): MultipartBody.Part {
        val requestFile = RequestBody.create(
            MediaType.parse("video/mp4"),
            fileUri
        )
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile)
    }
}