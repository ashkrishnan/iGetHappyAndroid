package com.example.singhrahuldeep.igethappy.views.signup

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityLanguageSelectionBinding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.LanguageResponse
import com.example.singhrahuldeep.igethappy.model.output.SignUpResponse
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.LanguageSignUpViewModel
import com.example.singhrahuldeep.igethappy.viewModel.LanguageViewModel
import com.example.singhrahuldeep.igethappy.views.carerecievers.CaregiverActivity
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.google.android.material.chip.Chip

/**
 * Created by Gharjyot Singh
 */


@Suppress("DEPRECATION")
class LanguageSelectionActivity : BaseActivity(), CallBackResult.LanguageSignupCallbacks {

    private var prefsHelper: SharedPrefsHelper? = null
    private var mSelectLang: ArrayList<String>? = null
    private var dataBundle: Bundle? = null
    private var binding: ActivityLanguageSelectionBinding? = null
    private var languageSignUpViewModel: LanguageSignUpViewModel? = null
    private var languageViewModel: LanguageViewModel? = null
    private var locale: String? = "US"


    override fun getLayoutId(): Int {
        return R.layout.activity_language_selection
    }

    override fun initViews() {
        getDataFromIntent()
        binding = viewDataBinding as ActivityLanguageSelectionBinding?
        languageSignUpViewModel = ViewModelProviders.of(this).get(LanguageSignUpViewModel::class.java)
        languageSignUpViewModel?.init(this)
        languageViewModel = ViewModelProviders.of(this).get(LanguageViewModel::class.java)
        languageViewModel?.init()
        binding!!.viewModel = languageSignUpViewModel
        binding!!.executePendingBindings()
        mSelectLang = ArrayList()
        locale = this.getResources().getConfiguration().locale.getCountry()



        languageSignUpViewModel?.getData()?.observe(this, object : Observer<SignUpResponse> {
            override fun onChanged(response: SignUpResponse?) {
                languageSignUpViewModel?.isLoading?.postValue(false)
                if (response != null) {
                    if (response.status == "200") {

                        prefsHelper = SharedPrefsHelper(this@LanguageSelectionActivity)
                        prefsHelper!!.save(PreferenceKeys.PREF_USER_ID, response.data!!._id)
                        prefsHelper!!.save(PreferenceKeys.PREF_ISANONYMOUS, response.data.is_anonymous)
                        prefsHelper!!.save(
                            PreferenceKeys.PREF_USER_NAME,
                            response.data.first_name + " " + response.data.last_name
                        )
                        prefsHelper!!.save(PreferenceKeys.PREF_USER_IMAGE, response.data.profile_image)
                        prefsHelper!!.save(PreferenceKeys.PREF_AUTHENTICATION_TOKEN, response.token)
                        val intent = Intent(this@LanguageSelectionActivity, CaregiverActivity::class.java)
                        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        showToast(response.message.toString())
                    }
                }
            }
        })

        languageSignUpViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        languageViewModel!!.fetchLanguages(locale.toString())

        languageViewModel?.getData()?.observe(this, object : Observer<LanguageResponse> {
            override fun onChanged(response: LanguageResponse?) {
                languageSignUpViewModel?.isLoading?.postValue(false)
                if (response != null) {
                    if (response.data !=null && response.data?.size != 0) {
                        if (response.data!!.get(0).languages != null) {
                            val languages = ArrayList<String>()
                            for (index in response.data!![0].languages!!.indices) {
                                languages.add(response.data!![0].languages!!.get(index).language_name!!)
                            }

                            getChip(languages)
                        } else {
                            showToast(getResources().getString(R.string.no_languages_found))
                        }
                    } else {
                        showToast(getResources().getString(R.string.device_country_not_registered))
                    }

                }
            }
        })

        languageViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })


    }

    fun getDataFromIntent() {
        if (intent != null)
            dataBundle = intent.getBundleExtra("data")
    }


    override fun onContinueClicked() {
        if (Utilities.checkInternet(this)) {
            if (mSelectLang!!.isNotEmpty()) {
                dataBundle!!.putStringArrayList("languages", mSelectLang)
                languageSignUpViewModel!!.doSignUp(dataBundle!!)
            } else {
                showToast(getString(R.string.select_language))
            }

        } else {
            showToast(getString(R.string.no_internet_msg))
        }

    }

    private fun getChip(languages: ArrayList<String>) {
        for (i in languages) {
            val chip = Chip(this)
            chip.setText(i)
            chip.chipEndPadding = 40F
            chip.chipStartPadding = 40F
            chip.chipStrokeColor = this.getResources().getColorStateList(R.color.black)
            chip.chipStrokeWidth = 1F
            chip.chipBackgroundColor = this.getResources().getColorStateList(R.color.white)

            chip.isClickable = true

            // Set the chip click listener
            chip.setOnClickListener {

                if (mSelectLang!!.contains("${chip.chipText}")) {
                    mSelectLang!!.remove("${chip.chipText}")
                    chip.chipBackgroundColor = this.getResources().getColorStateList(R.color.white)
                    chip.setTextColor(this.getResources().getColorStateList(R.color.black))
                    chip.chipStrokeWidth = 1F

                } else {
                    mSelectLang!!.add("${chip.chipText}")
                    chip.chipBackgroundColor = this.getResources().getColorStateList(R.color.blue_color)
                    chip.setTextColor(this.getResources().getColorStateList(R.color.white))
                    chip.chipStrokeWidth = 0F
                }
            }
            // Finally, add the chip to chip group
            binding!!.chipGroup.addView(chip)

        }
    }

    override fun onBackClicked() {
        finish()
    }

    override fun dispose() {
    }
}
