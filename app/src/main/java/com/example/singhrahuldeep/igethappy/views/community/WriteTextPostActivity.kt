package com.example.singhrahuldeep.igethappy.views.community

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.MediaController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityWriteTextPostBinding
import com.example.singhrahuldeep.igethappy.interfaces.AddPostCallback
import com.example.singhrahuldeep.igethappy.model.output.AddPostResponse
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.AddPostResponseViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_write_text_post.*
import kotlinx.android.synthetic.main.post_toolbar.*

/**
 * Created by Gharjyot Singh
 */


class WriteTextPostActivity : BaseActivity(), View.OnClickListener, AddPostCallback {


    private var addPostViewModel: AddPostResponseViewModel? = null
    var privacyStrings = arrayOf( "Private","Public")
    var privacyData = arrayOf( "PRIVATE","PUBLIC")
    var privacy = ""

    var binding: ActivityWriteTextPostBinding? = null
    var ivCancel: ImageView? = null
    private var prefsHelper: SharedPrefsHelper? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_write_text_post
    }

    override fun initViews() {
        postToolbarTitle.setText(getString(R.string.write_thoughts))


        binding = viewDataBinding as ActivityWriteTextPostBinding

        addPostViewModel = ViewModelProviders.of(this).get(AddPostResponseViewModel::class.java)
        addPostViewModel?.init(this)

        parentLayout = binding!!.clParent

        binding!!.viewModel = addPostViewModel


        ivCancel = findViewById(R.id.iv_cancel)
        ivCancel!!.setOnClickListener(this)

        prefsHelper = SharedPrefsHelper(this)
        val name = prefsHelper!!.get(PreferenceKeys.PREF_USER_NAME, "")
        val profile = prefsHelper!!.get(PreferenceKeys.PREF_USER_IMAGE, "dadsa")

        binding!!.tvName.text = name
        Picasso.with(this).load(profile).fit().centerCrop()
            .placeholder(R.drawable.ic_user_profle)
            .error(R.drawable.ic_user_profle)
            .into(binding!!.ivProfile)

        addPostViewModel?.getData()?.observe(this, object : Observer<AddPostResponse.Data> {
            override fun onChanged(response: AddPostResponse.Data?) {

                if (response != null) {
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


        val extraIntent = intent
        var path: String? = ""
        if (extraIntent != null) {
            path = extraIntent.getStringExtra(AppConstants.EXTRA_VIDEO_URI)

        }

        if (path != null && !path.isEmpty()) {
            postToolbarTitle.setText(getString(R.string.speak_thoughts))
            addPostViewModel!!.isVideoPost = true
            addPostViewModel!!.videoFilePath = path
            setUpVideoView(path)

        } else {
            addPostViewModel!!.isVideoPost = false
            postToolbarTitle.setText(getString(R.string.write_thoughts))
        }

        setSpinner()

        val isAnonymous = prefsHelper!!.get(PreferenceKeys.PREF_ISANONYMOUS, "")

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

    private fun setUpVideoView(path: String) {
        tv_heading.visibility = View.GONE
        videoView.visibility = View.VISIBLE
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoPath(path)
        videoView.requestFocus()
        videoView.start()
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_cancel -> {
                finish()
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

        showToast(getString(R.string.enter_description))
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


    override fun dispose() {

    }

}