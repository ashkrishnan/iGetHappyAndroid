package com.example.singhrahuldeep.igethappy

import com.example.singhrahuldeep.igethappy.model.input.LoginInputModel
import com.example.singhrahuldeep.igethappy.model.output.ChatBotResponseModel
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import com.example.singhrahuldeep.igethappy.model.output.VerifyNumberResponse
import retrofit2.Call

/**
 * Created by Gharjyot Singh
 */

class CallBackResult {

    interface LoaderCallbacks {
        fun setProgressBar()
        fun dismiss()
    }

    interface LoginEmailCallbacks {
        fun onResponseError(call: Call<LoginResponseModel>, t: Throwable)
        fun onResponseSuccess(response: retrofit2.Response<LoginResponseModel>?)
        fun onPhoneNumberEmailError()
        fun onPhoneNumberEmailEmptyError()
        fun onPasswordEmpty()
        fun onPasswordError()
        fun onPasswordInvallidLength()
        fun validateSuccess(verifyNumber: LoginInputModel)
        fun onResponseSuccessError(message: String?)
        fun onSignupClicked()
        fun onForgotClicked()
        fun onBackClicked()
    }

    interface LoginCallbacks {
        fun onResponseError(call: Call<LoginResponseModel>, t: Throwable)
        fun onResponseSuccess(response: retrofit2.Response<LoginResponseModel>?)
        fun onContinueWithEmailClicked()
    }

    interface WelcomeCallbacks {
        fun onBeginClicked()
    }

    interface VerifyNumberCallbacks {
        fun onVerifyNumberNotExist(response: retrofit2.Response<VerifyNumberResponse>)
        fun onVerifyNumberExistAndRegistered(response: retrofit2.Response<VerifyNumberResponse>)
        fun onVerifyNumberExistNotRegistered(response: retrofit2.Response<VerifyNumberResponse>)
        fun onVerifyNumberError(call: Call<VerifyNumberResponse>, t: Throwable)
    }

    interface SignUpCallbacks {
        fun onGetStartedClicked()
        fun emailEmpty()
        fun phoneEmpty()
        fun passEmpty()
        fun passLengthInvalid()
        fun isInValidPass()
        fun isInValidEmail()
        fun isInValidPhone()
        fun validSuccess(email: String, phone: String, pass: String)
        fun invalidPhoneEmail()
    }

    interface ForgotCallbacks {
        fun onBackClicked()
        fun onSubmitClicked()
        fun onFieldEmpty()
        fun onValidEmail(email: String)
        fun onValidPhone(phone: String)
        fun onInValidPhoneEmail()
        fun onForgotsucuess()
        fun onSuccessError()

    }

    interface SignUpProfileCallbacks {
        fun onFirstNameEmpty()
        fun onlastNameEmpty()
        fun onNickNameEmpty()
        fun onInvalidFirstName()
        fun onInvalidLastName()
        fun onInvalidNickName()
        fun onImageSelected(path: String)
        fun onDummyImageSelected(imageNo: Int)
        fun onValidationsSuccess(firstName: String, lastName: String, nickName: String)
    }

    interface SignUpProfile2Callbacks {
        fun onBeginClicked()
    }

    interface LanguageSignupCallbacks {
        fun onContinueClicked()
        fun onBackClicked()
    }

    interface ChatBotCallbacks {
        fun onResponseError(call: Call<ChatBotResponseModel>, t: Throwable)
        fun onResponseSuccess(response: retrofit2.Response<ChatBotResponseModel>?)
        fun onResponseSuccessError(message: String?)
        fun onMessageSent()
    }
}