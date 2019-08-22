package com.example.singhrahuldeep.igethappy.interfaces

/**
 * Created by Gharjyot Singh
 */


interface EditPostCallback {
    fun getPostText(): String
    fun getAnnoymousStatus(): String
    fun getPrivacyStatus(): String
    fun setDescriptionError()
    fun connectedToInternet(): Boolean
    fun setConnectionError()
    fun setValidationError(message: String)
    fun onVideoPathReady()
}