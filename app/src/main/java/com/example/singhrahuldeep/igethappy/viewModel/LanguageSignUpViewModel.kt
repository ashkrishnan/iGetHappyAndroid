package com.example.singhrahuldeep.igethappy.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.input.LoginInputModel
import com.example.singhrahuldeep.igethappy.model.output.SignUpResponse
import com.example.singhrahuldeep.igethappy.repositories.LanguageSignUpRepository
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.Utilities
import okhttp3.RequestBody
import java.io.File
/**
 * Created by Gharjyot Singh
 */


class LanguageSignUpViewModel : ViewModel() {

    var data: MutableLiveData<SignUpResponse>? = null
    var loginData: MutableLiveData<SignUpResponse>? = null
    var languageSignUpRepository: LanguageSignUpRepository? = null
    var languageSignUpCallbacks: CallBackResult.LanguageSignupCallbacks? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init(languageSignUpCallbacks: CallBackResult.LanguageSignupCallbacks) {
        this.languageSignUpCallbacks = languageSignUpCallbacks
        languageSignUpRepository = LanguageSignUpRepository()
        languageSignUpRepository?.initRepo(languageSignUpCallbacks)


    }

    val loading: LiveData<Boolean>
        get() = isLoading

    fun onBackClicked() {
        languageSignUpCallbacks!!.onBackClicked()
    }

    fun onSignupClicked() {
        languageSignUpCallbacks!!.onContinueClicked()
    }


    fun doSignUp(bundleData: Bundle) {
        if (bundleData != null) {
            val email = bundleData.get("email")
            val phone = bundleData.get("phone")
            val socialId = bundleData.get("social_id")
            val path = bundleData.get("image")

            val dummyImagePath = bundleData.get("dummy_image")
            val pass = bundleData.get("pass")
            val nickName = bundleData.get("nick_name")
            val firstName = bundleData.get("first_name")
            val lastName = bundleData.get("last_name")
            val anonymousStatus = bundleData.get("anonymous_status")
            val profession = bundleData.get("profession")
            val gender = bundleData.get("gender")
            val dob = bundleData.get("dob")
            // dob = "12-12-2018"
            val languages = bundleData.getStringArrayList("languages")
            var llngString = ""

            if (languages != null) {
                if (!languages.isEmpty()) {

                    for (i in languages) {
                        if (llngString == "")
                            llngString = i
                        else {
                            llngString = llngString + "," + i
                        }
                    }

                }
            }

            isLoading.postValue(true)
            val mHashMap = HashMap<String, RequestBody>()
            if (phone != null)
                mHashMap.put("phone", Utilities.createPartFromString(phone.toString()))
            if (email != null)
                mHashMap.put("email", Utilities.createPartFromString(email.toString()))
            if (socialId != null)
                mHashMap.put("social_id", Utilities.createPartFromString(socialId.toString()))

            mHashMap["password"] = Utilities.createPartFromString(pass.toString())
            mHashMap["nick_name"] = Utilities.createPartFromString(nickName.toString())
            mHashMap["first_name"] = Utilities.createPartFromString(firstName.toString())
            mHashMap["last_name"] = Utilities.createPartFromString(lastName.toString())
            mHashMap["is_anonymous"] = Utilities.createPartFromString(anonymousStatus.toString())
            mHashMap["profession"] = Utilities.createPartFromString(profession.toString())
            mHashMap["gender"] = Utilities.createPartFromString(gender.toString())
            mHashMap["dob"] = Utilities.createPartFromString(dob.toString())
            mHashMap["login_type"] = Utilities.createPartFromString("NATIVE")
            mHashMap.put("speciality", Utilities.createPartFromString("TEST"))
            mHashMap.put("work_experience", Utilities.createPartFromString("1"))
            mHashMap.put("language_preferences", Utilities.createPartFromString(llngString))
            mHashMap.put("role", Utilities.createPartFromString("PROFESSIONAL"))
            mHashMap.put("country", Utilities.createPartFromString("India"))

            //TODO add dynamic data up
            // mHashMap.put("language_preferences",)
            if (path != null) {
                languageSignUpRepository!!.signUpUser(
                    mHashMap,
                    loginData!!,
                    Utilities.prepareFilePart(AppConstants.PROFILE_IMAGE, File(path.toString()))
                )
            }
            if (dummyImagePath != null) {
                languageSignUpRepository!!.signUpUser(
                    mHashMap,
                    loginData!!,
                    Utilities.prepareFilePart(AppConstants.PROFILE_IMAGE, File(dummyImagePath.toString()))
                )
            }

        }
    }

    fun getData(): LiveData<SignUpResponse> {

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