package com.example.singhrahuldeep.igethappy.views.auth

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityForgotPasswordBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.ForgotPasswordResponse
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.ForgotPasswordViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity


/**
 * Created by Gharjyot Singh
 */

class ForgotPassword : BaseActivity(), View.OnTouchListener, CallBackResult.ForgotCallbacks {


    private var binding: ActivityForgotPasswordBinding? = null
    private var forgotPassViewModel: ForgotPasswordViewModel? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_forgot_password
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initViews() {
        binding = viewDataBinding as ActivityForgotPasswordBinding?
        forgotPassViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        forgotPassViewModel?.init(this)
        binding!!.viewModel = forgotPassViewModel
        binding!!.executePendingBindings()
        binding!!.clMain.setOnTouchListener(this)

        forgotPassViewModel?.getData()?.observe(this, object : Observer<ForgotPasswordResponse> {
            override fun onChanged(response: ForgotPasswordResponse?) {
                forgotPassViewModel?.isLoading?.postValue(false)
                if (response != null) {
                    if (response.status == "200") {
                        showToast(response.message.toString())
                    } else {
                        showToast(response.message.toString())
                    }
                }
            }
        })

        forgotPassViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

    }

    override fun onBackClicked() {
        finish()
    }

    override fun onFieldEmpty() {
        showToast(getString(R.string.please_enter))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Utilities.hideKeypad(v!!)
        return true
    }

    override fun onValidEmail(email: String) {
        if (Utilities.checkInternet(this)) {
            forgotPassViewModel!!.hitForgotEmail(email)
        } else {
            showToast(this.resources.getString(R.string.no_internet_msg))
        }
    }

    override fun onValidPhone(phone: String) {
        if (Utilities.checkInternet(this)) {
            forgotPassViewModel!!.hitForgotPhone(phone)
        } else {
            showToast(this.resources.getString(R.string.no_internet_msg))
        }
    }

    override fun onInValidPhoneEmail() {
        showToast(getString(R.string.please_enter_valid))
    }

    override fun dispose() {

    }

    override fun onSubmitClicked() {

    }

    override fun onForgotsucuess() {

    }

    override fun onSuccessError() {

    }
}
