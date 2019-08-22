package com.example.singhrahuldeep.igethappy.interfaces


/**
 * Created by Gharjyot Singh
 */


interface CareReceiverCallback {

    fun itemSelected(position: Int)
    fun setAvatar(position: Int)
    fun getName():String
    fun getPhone():String
    fun getEmail():String
    fun getMinorStatus():String
    fun getRelationship():String
    fun getProfileImage():String
    fun deleteCareReceiver(position: Int)
    fun connectedToInternet(): Boolean
    fun setConnectionError()
    fun setValidationError(type:String)

}