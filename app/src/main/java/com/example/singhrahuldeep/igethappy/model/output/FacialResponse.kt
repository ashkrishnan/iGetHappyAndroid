package com.example.singhrahuldeep.igethappy.model.output

class FacialResponse {

    var surprise: String? = null

    var happy: String? = null

    var sad: String? = null

    var prediction: String? = null

    var neutral: String? = null

    var angry: String? = null

    var disgust: String? = null

    var fear: String? = null

    override fun toString(): String {
        return "ClassPojo [surprise = $surprise, happy = $happy, sad = $sad, prediction = $prediction, neutral = $neutral, angry = $angry, disgust = $disgust, fear = $fear]"
    }
}
