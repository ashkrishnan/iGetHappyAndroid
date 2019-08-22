package com.example.singhrahuldeep.igethappy.views.community

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityAudioPostBinding
import com.example.singhrahuldeep.igethappy.audiorecorder.Storage
import com.example.singhrahuldeep.igethappy.interfaces.AddPostCallback
import com.example.singhrahuldeep.igethappy.model.output.AddPostResponse
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.AddPostResponseViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_audio_post.*
import kotlinx.android.synthetic.main.post_toolbar.*

/**
 * Created by Gharjyot Singh
 */


class WriteAudioPostActivity : BaseActivity(), View.OnClickListener, AddPostCallback {

    var storage: Storage? = null
    private var addPostViewModel: AddPostResponseViewModel? = null
    var binding: ActivityAudioPostBinding? = null
    var privacyStrings = arrayOf("Private", "Public")
    var privacyData = arrayOf("PRIVATE", "PUBLIC")
    var privacy = ""
    var ivCancel: ImageView? = null
    private var prefsHelper: SharedPrefsHelper? = null
    var openMic = false

    companion object {
        const val AUDIO_RECORD_CODE = 101
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_post
    }

    override fun initViews() {

        binding = viewDataBinding as ActivityAudioPostBinding

        addPostViewModel = ViewModelProviders.of(this).get(AddPostResponseViewModel::class.java)
        addPostViewModel?.init(this)

        parentLayout = binding!!.clParent

        binding!!.viewModel = addPostViewModel

        postToolbarTitle.setText(getString(R.string.speak_thoughts))
        ivMic.setOnClickListener(this)

        ivCancel = findViewById(R.id.iv_cancel)
        ivCancel!!.setOnClickListener(this)

        prefsHelper = SharedPrefsHelper(this)
        val name = prefsHelper!!.get(PreferenceKeys.PREF_USER_NAME, "")
        val profile = prefsHelper!!.get(PreferenceKeys.PREF_USER_IMAGE, "dasdsa")

        binding!!.tvName.text = name
        Picasso.with(this).load(profile).fit().centerCrop()
            .placeholder(R.drawable.ic_user_profle)
            .error(R.drawable.ic_user_profle)
            .into(binding!!.ivProfile)


        storage = Storage(this)

        if (permitted()) {
            storage!!.migrateLocalStorage()
        }

        addPostViewModel?.getData()?.observe(this, object : Observer<AddPostResponse.Data> {
            override fun onChanged(response: AddPostResponse.Data?) {

                if (response != null) {
                    binding!!.avPlayer.destroy()
                    openMessageDialog(getString(R.string.post_added_success))
                } else {

                    showToast(R.string.something_went_wrong)
                }

            }
        })

        addPostViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        setSpinner()

        val isAnonymous = prefsHelper!!.get(PreferenceKeys.PREF_ISANONYMOUS, "")
        Log.d("", "isAnonymous: ${isAnonymous}")
        binding!!.switchView.isChecked = isAnonymous.equals("YES")
        binding!!.switchView.trackDrawable = resources.getDrawable(R.drawable.round_switch_background_grey)

        binding!!.switchView.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                binding!!.switchView.trackDrawable = resources.getDrawable(R.drawable.round_switch_background_grey)
            } else {
                binding!!.switchView.trackDrawable = resources.getDrawable(R.drawable.round_switch_background)
            }
        }

    }

    fun setSpinner() {
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, privacyStrings)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding!!.spinPrivacy.adapter = arrayAdapter

        binding!!.spinPrivacy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                privacy = privacyData.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding!!.spinPrivacy.setSelection(0)

    }

    val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun permitted(): Boolean {
        for (s in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, s) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
                return false
            }
        }
        return true
    }

    override fun dispose() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.iv_cancel -> {
                finish()
            }

            R.id.ivMic -> {


                openMic = true
                if (openMic) {
                    ivMic.visibility = View.GONE
                    openMic = false
                    val intent = Intent(this@WriteAudioPostActivity, RecordingActivity::class.java)
                    startActivityForResult(intent, AUDIO_RECORD_CODE)
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUDIO_RECORD_CODE) {
            val path = data?.extras?.get("audioFilePath")
            if (path != null) {
                cl_audioView.visibility = View.VISIBLE
                addPostViewModel!!.videoFilePath = path.toString()

                binding!!.avPlayer.withUrl(path.toString())
            } else {
                ivMic.visibility = View.VISIBLE
                openMic = true
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {


            }
            else -> {

            }
        }
    }


    override fun getPostText(): String {
        return binding!!.tvDesc.text.toString()
    }

    override fun getAnnoymousStatus(): String {

        val annoymous: String

        if (binding!!.switchView.isChecked)
            annoymous = AppConstants.YES
        else
            annoymous = AppConstants.NO

        return annoymous
    }

    override fun getPrivacyStatus(): String {
        return privacy
    }

    override fun setDescriptionError() {

        binding!!.tvDesc.setError(getString(R.string.enter_description))
    }

    override fun connectedToInternet(): Boolean {

        return Utilities.checkInternet(this)
    }

    override fun setConnectionError() {
        showToast(R.string.no_internet_msg)
    }

    override fun setValidationError(message: String) {
        showToast(message)
    }


}