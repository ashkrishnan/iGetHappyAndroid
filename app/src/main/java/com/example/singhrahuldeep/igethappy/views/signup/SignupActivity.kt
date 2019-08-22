package com.example.singhrahuldeep.igethappy.views.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivitySignupBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.EmailExistModelResponse
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.SignUpViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity

/**
 * Created by Gharjyot Singh
 */


class SignupActivity : BaseActivity(), View.OnTouchListener, CallBackResult.SignUpCallbacks, View.OnClickListener {

    private var binding: ActivitySignupBinding? = null
    private var mSignUpViewModel: SignUpViewModel? = null
    private var prefsHelper: SharedPrefsHelper? = null
    private var mEmail: String? = ""
    private var mPhone: String? = ""
    private var mPass: String? = ""
    private var termsConsent: Boolean? = false

    override fun getLayoutId(): Int {
        return R.layout.activity_signup
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initViews() {
        binding = viewDataBinding as ActivitySignupBinding?
        mSignUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        mSignUpViewModel?.init(this)
        binding!!.viewModel = mSignUpViewModel
        binding!!.executePendingBindings()
        binding!!.rlRaferral.setOnClickListener(this)
        binding!!.ivBack.setOnClickListener(this)
        binding!!.tvTermsConditions.setOnClickListener(this)
        binding!!.RelativeLayout.setOnTouchListener(this)
        prefsHelper = SharedPrefsHelper(this)

        mSignUpViewModel?.verifyEmail()?.observe(this, object : Observer<EmailExistModelResponse> {
            override fun onChanged(response: EmailExistModelResponse?) {
                mSignUpViewModel?.isLoading?.postValue(false)
                if (response != null) {
                    if (response.status == "200") {
                        showToast(getString(R.string.email_already_exist))

                    } else {
                        mSignUpViewModel!!.checkPhoneExist(mPhone!!)
                    }
                }
            }
        })


        mSignUpViewModel?.verifyPhone()?.observe(this, object : Observer<EmailExistModelResponse> {
            override fun onChanged(response: EmailExistModelResponse?) {
                mSignUpViewModel?.isLoading?.postValue(false)
                if (response != null) {
                    if (response.status == "200") {
                        showToast(getString(R.string.phone_already_exist))

                    } else {
                        val intent = Intent(this@SignupActivity, SignupProfileActivity::class.java)
                        intent.putExtra("phone", mPhone)
                        intent.putExtra("email", mEmail)
                        intent.putExtra("pass", mPass)
                        startActivity(intent)
                    }
                }
            }
        })

        mSignUpViewModel?.isLoading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        binding!!.switchTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            termsConsent = isChecked
            if (isChecked) {
                binding!!.switchTerms.trackDrawable = resources.getDrawable(R.drawable.round_switch_background_grey)
            } else {
                binding!!.switchTerms.trackDrawable = resources.getDrawable(R.drawable.round_switch_background)
            }
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_raferral -> {
                showToast(this.resources.getString(R.string.coming_soon))
            }

            R.id.iv_back -> {
                finish()
            }

            R.id.tv_terms_conditions -> {
                showToast(getString(R.string.coming_soon))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Utilities.hideKeypad(v!!)
        return true
    }

    override fun onGetStartedClicked() {

    }

    override fun emailEmpty() {
        showToast(getString(R.string.please_enter_email))
    }

    override fun passLengthInvalid() {
        showToast(getString(R.string.please_enter_8_char_pass))
    }

    override fun isInValidEmail() {
        showToast(getString(R.string.invalid_email))
    }

    override fun isInValidPhone() {
        showToast(getString(R.string.invalid_phone))
    }

    override fun phoneEmpty() {
        showToast(getString(R.string.enter_phone))
    }

    override fun validSuccess(email: String, phone: String, pass: String) {
        mEmail = email
        mPhone = phone
        mPass = pass

        if (Utilities.checkInternet(this)) {
            if (termsConsent!!) {
                mSignUpViewModel!!.checkEmailExist(email)
            } else {
                showToast(this.resources.getString(R.string.agree_terms))
            }
        } else {
            showToast(this.resources.getString(R.string.no_internet_msg))
        }

    }

    override fun invalidPhoneEmail() {
        showToast(getString(R.string.please_enter_valid))
    }

    override fun isInValidPass() {
        showToast(getString(R.string.password_invalid))
    }

    override fun passEmpty() {
        showToast(getString(R.string.please_enter_pass))
    }

    override fun dispose() {
    }

}