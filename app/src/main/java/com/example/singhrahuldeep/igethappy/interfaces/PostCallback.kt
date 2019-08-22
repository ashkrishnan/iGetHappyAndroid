package com.example.singhrahuldeep.igethappy.interfaces

import com.example.singhrahuldeep.igethappy.model.input.UpdatePostInputModel
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple
import okhttp3.RequestBody

/**
 * Created by Gharjyot Singh
 */


interface PostCallback {

    fun getInstance(position: Int)
    fun likePost(mHashMap: HashMap<String, RequestBody>)
    fun openMenuDialog(post_id: String, model: UpdatePostInputModel)
    fun noConnection()
}