package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.input.LoginInputModel
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import com.example.singhrahuldeep.igethappy.repositories.LoginEmailRepository
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.validations.LoginEmailValidation
import okhttp3.RequestBody
/**
 * Created by Gharjyot Singh
 */


class LoginEmailViewModel : ViewModel() {

    var data: MutableLiveData<LoginResponseModel>? = null
    var loginData: MutableLiveData<LoginResponseModel>? = null
    var loginEmailRepo: LoginEmailRepository? = null
    var loginEmailCallbacks: CallBackResult.LoginEmailCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(loginEmailCallbacks: CallBackResult.LoginEmailCallbacks) {
        this.loginEmailCallbacks = loginEmailCallbacks
        loginEmailRepo = LoginEmailRepository()
        loginEmailRepo?.initRepo(loginEmailCallbacks)
        //if (data != null) return
        // dataList = verifyNumberRepo?.checkNUmberExists("")
    }

    val loading: LiveData<Boolean>
        get() = isLoading

    fun onLoginClicked(email: String, password: String) {
        val loginValidations = LoginEmailValidation(loginEmailCallbacks)
        loginValidations.checkLoginValidation(email, password)
    }

    fun onSignupClicked() {
        loginEmailCallbacks!!.onSignupClicked()
    }

    fun onForgotClicked() {
        loginEmailCallbacks!!.onForgotClicked()
    }

    fun onBackClicked() {
        loginEmailCallbacks!!.onBackClicked()
    }

    fun doLogin(inputLogin: LoginInputModel) {
        isLoading.postValue(true)
        val mHashMap = HashMap<String, RequestBody>()
        if (inputLogin.email != null)
            mHashMap.put("email", Utilities.createPartFromString(inputLogin.email.toString()))
        if (inputLogin.phone != null)
            mHashMap.put("phone", Utilities.createPartFromString(inputLogin.phone.toString()))

        mHashMap.put("password", Utilities.createPartFromString(inputLogin.password.toString()))

        loginEmailRepo!!.loginUser(mHashMap, loginData!!)
    }

    fun getData(): LiveData<LoginResponseModel> {

        if (loginData == null) {
            loginData = MutableLiveData()
        }

        return loginData!!
    }

    private fun doCheckForLogin(phoneNumber: CharSequence) {
        val inputLogin = LoginInputModel()
        inputLogin.email = phoneNumber.toString()
        //  dataList = loginEmailRepo?.checkNUmberExists(phoneNumber.toString())

    }


}