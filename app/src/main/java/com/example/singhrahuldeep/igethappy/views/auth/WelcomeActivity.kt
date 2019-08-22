package com.example.singhrahuldeep.igethappy.views.auth

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityWelcomeBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.LoginResponseModel
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.viewModel.WelcomeViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.example.singhrahuldeep.igethappy.views.signup.SignupProfileActivity
/**
 * Created by Gharjyot Singh
 */

class WelcomeActivity : BaseActivity(), CallBackResult.WelcomeCallbacks {

    private var mSocialId: String? = null
    private var mEmail: String? = null
    private var mFirstName: String? = null
    private var mLastName: String? = null

    private var binding: ActivityWelcomeBinding? = null
    private var welcomeViewModel: WelcomeViewModel? = null
    private var prefsHelper: SharedPrefsHelper? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {

        binding = viewDataBinding as ActivityWelcomeBinding?
        welcomeViewModel = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)
        welcomeViewModel?.init(this)
        binding!!.viewModel = welcomeViewModel
        binding!!.executePendingBindings()
        prefsHelper = SharedPrefsHelper(this)

        welcomeViewModel?.list?.observe(this, object : Observer<LoginResponseModel> {
            override fun onChanged(response: LoginResponseModel?) {

            }
        })
        welcomeViewModel?.isLoading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        if (intent != null) {
            mSocialId = intent.getStringExtra("social_id")
            mEmail = intent.getStringExtra("email")
            mFirstName = intent.getStringExtra("first_name")
            mLastName = intent.getStringExtra("last_name")
        }
        if (mFirstName != null)
            binding!!.tvHiText.setText("Hi," + mFirstName)

    }

    override fun onBeginClicked() {
        val intent = Intent(this, SignupProfileActivity::class.java)
        intent.putExtra("social_id", mSocialId)
        //intent.putExtra("email", mEmail)
        intent.putExtra("pass", "igethappy@123")
        intent.putExtra("first_name", mFirstName)
        intent.putExtra("last_name", mLastName)
        startActivity(intent)
    }


    override fun dispose() {

    }

}
