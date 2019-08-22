package com.example.singhrahuldeep.igethappy.model.output

class AddCareReceiverResponse {

    var data: Data? = null

    var message: String? = null

    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [data = $data, message = $message, status = $status]"
    }

    inner class Data {
        var is_care_receiver_minor: String? = null

        var created_at: String? = null

        var password: String? = null

        var profile_image: String? = null

        var updated_at: String? = null

        var phone: String? = null

        var __v: String? = null

        var first_name: String? = null

        var _id: String? = null

        var relationship: String? = null

        var care_giver_id: String? = null

        var email: String? = null

        var status: String? = null

        override fun toString(): String {
            return "ClassPojo [is_care_receiver_minor = $is_care_receiver_minor, created_at = $created_at, password = $password, profile_image = $profile_image, updated_at = $updated_at, phone = $phone, __v = $__v, name = $first_name, _id = $_id, relationship = $relationship, care_giver_id = $care_giver_id, email = $email, status = $status]"
        }
    }
}
