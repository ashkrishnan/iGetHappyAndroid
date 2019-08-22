package com.example.singhrahuldeep.igethappy.views.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityLoginEmailBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.input.LoginInputModel
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.LoginEmailViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.example.singhrahuldeep.igethappy.views.dashboard.LandingPageActivity
import com.example.singhrahuldeep.igethappy.views.signup.SignupActivity
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Gharjyot Singh
 */

class LoginEmailActivity : BaseActivity(), View.OnTouchListener, CallBackResult.LoginEmailCallbacks {

    private var binding: ActivityLoginEmailBinding? = null
    private var loginEmailViewModel: LoginEmailViewModel? = null
    private var prefsHelper: SharedPrefsHelper? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_login_email
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initViews() {
        binding = viewDataBinding as ActivityLoginEmailBinding?
        loginEmailViewModel = ViewModelProviders.of(this).get(LoginEmailViewModel::class.java)
        loginEmailViewModel?.init(this)
        binding!!.viewModel = loginEmailViewModel
        binding!!.executePendingBindings()
        binding!!.RelativeLayout.setOnTouchListener(this)
        prefsHelper = SharedPrefsHelper(this)

        loginEmailViewModel?.getData()?.observe(this, object : Observer<LoginResponseModel> {
            override fun onChanged(response: LoginResponseModel?) {
                loginEmailViewModel?.isLoading?.postValue(false)
                if (response != null) {
                    if (response.status == "200") {
                        //  Constants.LOGIN_TOKEN = response?.body()?.ResultData!!.Token!!
                        prefsHelper!!.save(PreferenceKeys.PREF_AUTHENTICATION_TOKEN, response.token)
                        prefsHelper!!.save(PreferenceKeys.PREF_USER_ID, response.data!!._id)
                        prefsHelper!!.save(PreferenceKeys.PREF_ISANONYMOUS, response.data.is_anonymous)
                        prefsHelper!!.save(
                            PreferenceKeys.PREF_USER_NAME,
                            response.data.first_name + " " + response.data.last_name
                        )
                        prefsHelper!!.save(PreferenceKeys.PREF_USER_IMAGE, response.data.profile_image)
                        val intent = Intent(this@LoginEmailActivity, LandingPageActivity::class.java)
                        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        showToast(response.message.toString())
                    }
                }
            }
        })

        loginEmailViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Utilities.hideKeypad(v!!)
        return true
    }


    override fun dispose() {
    }

    override fun onSignupClicked() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    override fun onForgotClicked() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
    }

    override fun onBackClicked() {
        finish()
    }

    override fun onResponseError(call: Call<LoginResponseModel>, t: Throwable) {
        showToast(t.message!!)
    }

    override fun onResponseSuccess(response: Response<LoginResponseModel>?) {
        loginEmailViewModel?.isLoading?.postValue(false)
        //  Constants.LOGIN_TOKEN = response?.body()?.ResultData!!.Token!!
        prefsHelper!!.save(PreferenceKeys.PREF_AUTHENTICATION_TOKEN, response!!.body().token)
        val intent = Intent(this, LandingPageActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    override fun onResponseSuccessError(message: String?) {
        loginEmailViewModel?.isLoading?.postValue(false)
        showToast(message!!)
    }

    override fun onPhoneNumberEmailError() {
        // binding!!.etEmail.requestFocus()
        //  binding!!.etEmail.setError(getString(R.string.please_enter_valid))
        showToast(getString(R.string.please_enter_valid))
    }

    override fun onPasswordEmpty() {
        showToast(getString(R.string.please_enter_pass))
    }


    override fun onPhoneNumberEmailEmptyError() {
        showToast(getString(R.string.please_enter))
    }

    override fun onPasswordError() {
        showToast(getString(R.string.password_invalid))
    }

    override fun onPasswordInvallidLength() {
        showToast(getString(R.string.please_enter_8_char_pass))
    }


    override fun validateSuccess(verifyNumber: LoginInputModel) {
        if (Utilities.checkInternet(this)) {
            loginEmailViewModel?.doLogin(verifyNumber)
        } else {
            showToast(this.resources.getString(R.string.no_internet_msg))
        }
    }
}
