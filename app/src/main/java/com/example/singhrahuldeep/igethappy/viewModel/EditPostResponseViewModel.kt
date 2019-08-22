package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.interfaces.EditPostCallback
import com.example.singhrahuldeep.igethappy.model.input.UpdatePostInputModel
import com.example.singhrahuldeep.igethappy.model.output.AddPostResponse
import com.example.singhrahuldeep.igethappy.repositories.EditPostRepository
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.Utilities
import okhttp3.RequestBody
import java.io.File
/**
 * Created by Gharjyot Singh
 */

class EditPostResponseViewModel : ViewModel() {

    var dataResponse: MutableLiveData<AddPostResponse>? = null
    private var allData: MutableLiveData<AddPostResponse.Data>? = null
    var postRepo: EditPostRepository? = null
    private val mIsUpdating = MutableLiveData<Boolean>()
    var isVideoPost = false
    var videoFilePath = ""
    var callback: EditPostCallback? = null
    var model: UpdatePostInputModel? = null
    var postId = ""

    fun init(
        callback: EditPostCallback,
        model: UpdatePostInputModel,
        postId: String
    ) {
        this.postId = postId
        this.model = model
        this.callback = callback
        postRepo = EditPostRepository()
        postRepo?.initRepo()

        if (dataResponse != null) return
    }

    fun updatePost() {

        if (checkValidations()) {
            mIsUpdating.postValue(true)
            val mHashMap = HashMap<String, RequestBody>()
            mHashMap.put(AppConstants.USER_ID, Utilities.createPartFromString(AppConstants.APP_USER_ID))
            mHashMap.put(AppConstants.DESCRIPTION, Utilities.createPartFromString(callback!!.getPostText()))
            mHashMap.put(AppConstants.PRIVACY_OPTION, Utilities.createPartFromString(callback!!.getPrivacyStatus()))
            mHashMap.put(AppConstants.IS_ANONYMOUS, Utilities.createPartFromString(callback!!.getAnnoymousStatus()))
            mHashMap.put(
                AppConstants.POST_UPLOAD_TYPE,
                Utilities.createPartFromString(model!!.post_upload_type.toString())
            )
            if (model!!.post_upload_type.equals(AppConstants.VIDEO)) {
                val file = Utilities.prepareVideoFilePart(AppConstants.POST_UPLOAD_FILE, File(model!!.post_upload_file))
                postRepo!!.updatePost(postId, mHashMap, allData!!, mIsUpdating, file)

            } else if (model!!.post_upload_type.equals(AppConstants.AUDIO)) {
                val file = Utilities.prepareAudioFilePart(AppConstants.POST_UPLOAD_FILE, File(model!!.post_upload_file))
                postRepo!!.updatePost(postId, mHashMap, allData!!, mIsUpdating, file)

            } else {
                model!!.description = callback!!.getPostText()
                model!!.privacy_option = callback!!.getPrivacyStatus()
                model!!.is_anonymous = callback!!.getAnnoymousStatus()

                postRepo!!.updateTextPost(postId, model!!, allData!!, mIsUpdating)
            }
        } else {
            callback!!.setDescriptionError()
        }
    }


    fun checkValidations(): Boolean {
        val description = callback!!.getPostText()

        return description.isNotEmpty()

    }

    fun onAddPost() {
        if (callback!!.connectedToInternet()) {

            if (model!!.post_upload_type.equals(AppConstants.VIDEO)) {
                updatePost()
            } else if (model!!.post_upload_type.equals(AppConstants.AUDIO)) {
                updatePost()
            } else {
                updatePost()
            }
        } else {
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

}