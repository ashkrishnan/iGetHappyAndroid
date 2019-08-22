package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
/**
 * Created by Gharjyot Singh
 */

class SignUpProfile2ViewModel() : ViewModel() {

    var signUpProfile2Callbacks: CallBackResult.SignUpProfile2Callbacks? = null


    fun init(signUpProfile2Callbacks: CallBackResult.SignUpProfile2Callbacks) {
        this.signUpProfile2Callbacks = signUpProfile2Callbacks
    }

}