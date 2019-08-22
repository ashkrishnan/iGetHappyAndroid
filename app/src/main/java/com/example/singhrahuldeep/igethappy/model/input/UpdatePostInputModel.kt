package com.example.singhrahuldeep.igethappy.model.input

import java.io.Serializable

/**
 * Created by Gharjyot Singh
 */


class UpdatePostInputModel: Serializable {

    var user_id: String? = null
    var description: String? = null
    var privacy_option: String? = null
    var is_anonymous: String? = null
    var post_upload_type: String? = null
    var post_upload_file: String? = null
}