package com.example.singhrahuldeep.igethappy.model.output

class AddPostResponse {

    var data: Data? = null

    var message: String? = null

    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [data = $data, message = $message, status = $status]"
    }

    inner class Data {
        var post_upload_type: String? = null

        var updated_at: String? = null

        var user_id: String? = null

        var is_anonymous: String? = null

        var privacy_option: String? = null

        var __v: String? = null

        var created_at: String? = null

        var description: String? = null

        var _id: String? = null

        var post_upload_file: String? = null

        var status: String? = null

        override fun toString(): String {
            return "ClassPojo [post_upload_type = $post_upload_type, updated_at = $updated_at, user_id = $user_id, is_anonymous = $is_anonymous, privacy_option = $privacy_option, __v = $__v, created_at = $created_at, description = $description, _id = $_id, post_upload_file = $post_upload_file, status = $status]"
        }
    }

}
