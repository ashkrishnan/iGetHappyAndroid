package com.example.singhrahuldeep.igethappy.model.output

class LanguageResponse {

    var data: Array<Data>? = null

    var message: String? = null

    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [data = $data, message = $message, status = $status]"
    }

    inner class Data {
        var country_code: String? = null

        var updated_at: String? = null

        var languages: ArrayList<Languages>? = null

        var __v: String? = null

        var country_name: String? = null

        var created_at: String? = null

        var _id: String? = null

        var status: String? = null

        override fun toString(): String {
            return "ClassPojo [country_code = $country_code, updated_at = $updated_at, languages = $languages, __v = $__v, country_name = $country_name, created_at = $created_at, _id = $_id, status = $status]"
        }
    }


    inner class Languages {
        var updated_at: String? = null

        var created_at: String? = null

        var _id: String? = null

        var language_name: String? = null

        var status: String? = null

        override fun toString(): String {
            return "ClassPojo [updated_at = $updated_at, created_at = $created_at, _id = $_id, language_name = $language_name, status = $status]"
        }
    }

}
