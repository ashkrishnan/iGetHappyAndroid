package com.example.singhrahuldeep.igethappy.model.output

class LikePostResponseModel {

    var data: Data? = null

    var message: String? = null

    var status: String? = null

    var updatedRecordsCount: String? = null

    override fun toString(): String {
        return "ClassPojo [data = $data, message = $message, status = $status, updatedRecordsCount = $updatedRecordsCount]"
    }

    inner class Data {
        var updated_at: String? = null

        var post_id: String? = null

        var user_id: String? = null

        var __v: String? = null

        var created_at: String? = null

        var _id: String? = null

        var like_type: String? = null

        var status: String? = null

        override fun toString(): String {
            return "ClassPojo [updated_at = $updated_at, post_id = $post_id, user_id = $user_id, __v = $__v, created_at = $created_at, _id = $_id, like_type = $like_type, status = $status]"
        }
    }

}
