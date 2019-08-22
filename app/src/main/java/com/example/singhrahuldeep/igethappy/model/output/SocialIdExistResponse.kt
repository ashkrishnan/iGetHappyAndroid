package com.example.singhrahuldeep.igethappy.model.output

import androidx.databinding.BaseObservable

class SocialIdExistResponse() : BaseObservable() {
    val data: Data? = null
    val message: String? = null
    val status: String? = null

    inner class Data() : BaseObservable() {
        val available_days: Array<String>? = null
        val country: String? = null
        val role: String? = null
        val gender: String? = null
        val login_type: String? = null
        val language_preferences: Array<String>? = null
        val speciality: String? = null
        val password: String? = null
        val profile_image: String? = null
        val updated_at: String? = null
        val __v: String? = null
        val first_name: String? = null
        val email: String? = null
        val work_experience: String? = null
        val time_slot: Array<String>? = null
        val profession: String? = null
        val last_name: String? = null
        val social_id: String? = null
        val phone: String? = null
        val is_anonymous: String? = null
        val dob: String? = null
        val nick_name: String? = null
        val referral_code: String? = null
        val _id: String? = null
        val status: String? = null

    }

}