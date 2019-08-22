package com.example.singhrahuldeep.igethappy.model.output

class GetPostResponse {

    var data: ArrayList<Data>? = null

    var message: String? = null

    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [data = $data, message = $message, status = $status]"
    }

    inner class Data {
        var post_upload_type: String? = null

        var like_user_id_arr: Array<String>? = null

        var created_at: String? = null

        var description: String? = null

        var like_type_arr: Array<String>? = null

        var liked: String? = null

        var numOfLikes: String? = null

        var user_id: String? = null

        var is_anonymous: String? = null

        var userimage: String? = null

        var privacy_option: String? = null

        var liked_type: String? = null

        var _id: String? = null

        var time: String? = null

        var postfile: String? = null

        var status: String? = null

        var username: String? = null

        override fun toString(): String {
            return "ClassPojo [post_upload_type = $post_upload_type, like_user_id_arr = $like_user_id_arr, created_at = $created_at, description = $description, like_type_arr = $like_type_arr, liked = $liked, numOfLikes = $numOfLikes, user_id = $user_id, is_anonymous = $is_anonymous, userimage = $userimage, privacy_option = $privacy_option, liked_type = $liked_type, _id = $_id, time = $time, postfile = $postfile, status = $status, username = $username]"
        }
    }


}
