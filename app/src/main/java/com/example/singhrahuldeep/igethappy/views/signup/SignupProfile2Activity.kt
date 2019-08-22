package com.example.singhrahuldeep.igethappy.views.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivitySignupProfile2Binding
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.viewModel.SignUpProfile2ViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import java.util.*

/**
 * Created by Gharjyot Singh
 */


@Suppress("NAME_SHADOWING")
class SignupProfile2Activity : BaseActivity(), CallBackResult.SignUpProfile2Callbacks, View.OnClickListener,
    DatePicker.OnDateChangedListener {

    private var binding: ActivitySignupProfile2Binding? = null
    private var signUpProfile2ViewModel: SignUpProfile2ViewModel? = null
    private var calendar: Calendar = Calendar.getInstance()
    private var dataBundle: Bundle? = null
    private var gender: String? = "MALE"
    private var date: String? = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_signup_profile2
    }

    override fun initViews() {
        binding = viewDataBinding as ActivitySignupProfile2Binding?
        getDataFromIntent()
        signUpProfile2ViewModel = ViewModelProviders.of(this).get(SignUpProfile2ViewModel::class.java)
        signUpProfile2ViewModel?.init(this)
        binding!!.viewModel = signUpProfile2ViewModel
        binding!!.executePendingBindings()
        binding!!.ivMale.setOnClickListener(this)
        binding!!.ivFemale.setOnClickListener(this)
        binding!!.btnContinue.setOnClickListener(this)
        binding!!.ivBack.setOnClickListener(this)

        calendar.add(Calendar.YEAR, -13)
        binding!!.datePicker.maxDate = calendar.timeInMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding!!.datePicker.setOnDateChangedListener(this)
        }

    }

    override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val day = dayOfMonth.toString()
        val month = monthOfYear.toString()
        val year = year.toString()

        date = "$year-$month-$day"
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_male -> {
                binding!!.ivMaleTick.visibility = View.VISIBLE
                binding!!.ivFemaleTick.visibility = View.INVISIBLE
                binding!!.tvFemale.setTextColor(resources.getColor(R.color.grey_transparent_50))
                binding!!.tvMale.setTextColor(resources.getColor(R.color.black))
                gender = AppConstants.MALE
            }

            R.id.iv_back -> {
                finish()
            }

            R.id.iv_female -> {
                binding!!.ivMaleTick.visibility = View.INVISIBLE
                binding!!.ivFemaleTick.visibility = View.VISIBLE
                binding!!.tvFemale.setTextColor(resources.getColor(R.color.black))
                binding!!.tvMale.setTextColor(resources.getColor(R.color.grey_transparent_50))
                gender = AppConstants.FEMALE
            }

            R.id.btn_continue -> {
                val day = binding!!.datePicker.dayOfMonth.toString()
                val month = binding!!.datePicker.month + 1
                val year = binding!!.datePicker.year.toString()

                date = year + "-" + month.toString() + "-" + day


                if (dataBundle != null) {
                    dataBundle!!.putString("gender", gender)
                    dataBundle!!.putString("dob", date)
                }

                val intent = Intent(this@SignupProfile2Activity, LanguageSelectionActivity::class.java)
                intent.putExtra("data", dataBundle)
                startActivity(intent)
            }
        }
    }

    fun getDataFromIntent() {
        if (intent != null) {
            dataBundle = intent.getBundleExtra("data")
        }
    }

    override fun onBeginClicked() {

    }

    override fun dispose() {
    }
}
