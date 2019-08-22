package com.example.singhrahuldeep.igethappy.views.signup

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivitySignupProfileBinding
import com.bumptech.glide.Glide
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.adapters.DummyImageAdapter
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.CheckRuntimePermissions
import com.example.singhrahuldeep.igethappy.utils.FileUtils
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.ImageUploadModel
import com.example.singhrahuldeep.igethappy.viewModel.SignUpProfileViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import java.io.File

/**
 * Created by Gharjyot Singh
 */

class SignupProfileActivity : BaseActivity(), View.OnTouchListener, CallBackResult.SignUpProfileCallbacks,
    View.OnClickListener {

    private var binding: ActivitySignupProfileBinding? = null
    private var signUpProfileViewModel: SignUpProfileViewModel? = null
    private var imageUpload: ImageUploadModel? = null
    private var photos: Array<Int>? = null
    private var socialId: String? = null
    private var email: String? = null
    private var pass: String? = null
    private var phone: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    var PERMISSION_REQUEST_CODE = 200

    var path: String? = null
    private var imageNo: Int = 0
    var anonymousStatus = "NO"
    private var dummyImageAdapter: DummyImageAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_signup_profile
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initViews() {
        getDataFromIntent()
        binding = viewDataBinding as ActivitySignupProfileBinding?
        signUpProfileViewModel = ViewModelProviders.of(this).get(SignUpProfileViewModel::class.java)
        signUpProfileViewModel?.init(this)
        binding!!.viewModel = signUpProfileViewModel
        binding!!.executePendingBindings()
        binding!!.ivBack.setOnClickListener(this)
        binding!!.ivCamera.setOnClickListener(this)
        binding!!.ivQuestion.setOnClickListener(this)
        binding!!.RelativeLayout.setOnTouchListener(this)
        setDataOnUi()

        photos = arrayOf(
            R.drawable.ic_man_black,
            R.drawable.ic_man_redd,
            R.drawable.ic_angelll,
            R.drawable.ic_earmuffss,
            R.drawable.ic_elf
        )
        val profession = arrayOf("I'm a", "Doctor", "Teacher", "Engineer")

        dummyImageAdapter = DummyImageAdapter(this, photos!!, binding!!, this)
        binding!!.rvCharacter.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        binding!!.rvCharacter.setAdapter(dummyImageAdapter)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, profession)
        binding!!.spinnerIm.adapter = arrayAdapter

        binding!!.spinnerIm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        binding!!.switchTerms.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                binding!!.switchTerms.trackDrawable = resources.getDrawable(R.drawable.round_switch_background_grey)
                anonymousStatus = AppConstants.YES
            } else {
                binding!!.switchTerms.trackDrawable = resources.getDrawable(R.drawable.round_switch_background)
                anonymousStatus = AppConstants.NO
            }
        }

    }

    fun getDataFromIntent() {
        if (intent != null) {
            socialId = intent.getStringExtra("social_id")
            pass = intent.getStringExtra("pass")
            email = intent.getStringExtra("email")
            phone = intent.getStringExtra("phone")
            firstName = intent.getStringExtra("first_name")
            lastName = intent.getStringExtra("last_name")
        }

    }

    fun setDataOnUi() {
        if (firstName != null) {
            binding!!.etFirstName.setText(firstName)
        }
        if (lastName != null) {
            binding!!.etLastName.setText(lastName)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Utilities.hideKeypad(v!!)
        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_camera -> {
                imageUpload = ImageUploadModel(this, this)
                imageUpload!!.getImage()
            }
            R.id.iv_question -> {
                showToast(getString(R.string.coming_soon))
            }
            R.id.iv_back -> {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUpload!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onImageSelected(path: String) {
        this.path = path
        Glide.with(this).load(path).into(binding!!.ivImage)

    }

    override fun onDummyImageSelected(imageNo: Int) {
        this.imageNo = imageNo
    }


    override fun onFirstNameEmpty() {
        showToast(getString(R.string.invalid_first_name))
    }

    override fun onlastNameEmpty() {
        showToast(getString(R.string.empty_Last_name))
    }

    override fun onNickNameEmpty() {
        showToast(getString(R.string.empty_nick_name))
    }

    override fun onInvalidFirstName() {
        showToast(getString(R.string.invalid_length_first_name))
    }

    override fun onInvalidLastName() {
        showToast(getString(R.string.invalid_length_last_name))
    }

    override fun onInvalidNickName() {
        showToast(getString(R.string.invalid_length_nick_name))
    }

    fun getProfileImage(): String {

        val bm = getBitmapFromVectorDrawable(this, getVectorDrawables())

        val dir = File(Environment.getExternalStorageDirectory().toString() + File.separator + "drawable")

        if (!dir.exists()) {
            dir.mkdirs()
        }

        val path = FileUtils.saveBitmapToFile(
            dir,
            "avatar_${System.currentTimeMillis()}.png",
            bm,
            Bitmap.CompressFormat.PNG,
            100
        )

        return path

    }

    fun getVectorDrawables(): Int {
        when (imageNo) {
            0 -> {
                return R.drawable.ic_man_black
            }
            1 -> {
                return R.drawable.ic_man_redd
            }
            2 -> {
                return R.drawable.ic_angelll
            }
            3 -> {
                return R.drawable.ic_earmuffss
            }
            4 -> {
                return R.drawable.ic_elf
            }
        }
        return 0

    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(
            drawable!!.getIntrinsicWidth(),
            drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)

        return bitmap
    }


    override fun onValidationsSuccess(firstName: String, lastName: String, nickName: String) {

        if (CheckRuntimePermissions.checkMashMallowPermissions(
                baseActivity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_CODE
            )
        ) {
            val profession = binding!!.spinnerIm.selectedItem.toString()

            if (profession == "I'm a") {
                showToast(getString(R.string.select_profession))
            } else {
                val intent = Intent(this@SignupProfileActivity, SignupProfile2Activity::class.java)
                val b = Bundle()
                if (phone != null)
                    b.putString("phone", phone)
                if (email != null)
                    b.putString("email", email)
                if (socialId != null)
                    b.putString("social_id", socialId)
                if (path != null)
                    b.putString("image", path)
                else
                    b.putString("dummy_image", getProfileImage())

                b.putString("pass", pass)
                b.putString("nick_name", nickName)
                b.putString("first_name", firstName)
                b.putString("last_name", lastName)
                b.putString("anonymous_status", anonymousStatus)
                b.putString("profession", profession)

                intent.putExtra("data", b)
                startActivity(intent)
            }

        }
    }


    override fun dispose() {

    }

}
