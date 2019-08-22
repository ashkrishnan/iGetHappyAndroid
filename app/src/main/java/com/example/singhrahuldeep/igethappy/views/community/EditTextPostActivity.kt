package com.example.singhrahuldeep.igethappy.views.community

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MediaController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityEditTextPostBinding
import com.example.singhrahuldeep.igethappy.interfaces.EditPostCallback
import com.example.singhrahuldeep.igethappy.model.input.UpdatePostInputModel
import com.example.singhrahuldeep.igethappy.model.output.AddPostResponse
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.EditPostResponseViewModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_text_post.*
import kotlinx.android.synthetic.main.activity_write_text_post.tv_heading
import kotlinx.android.synthetic.main.activity_write_text_post.videoView
import kotlinx.android.synthetic.main.post_toolbar.*
import vn.tungdx.mediapicker.MediaItem
import vn.tungdx.mediapicker.MediaOptions
import vn.tungdx.mediapicker.activities.MediaPickerActivity

/**
 * Created by Gharjyot Singh
 */


class EditTextPostActivity : BaseActivity(), View.OnClickListener, EditPostCallback {


    private var editPostViewModel: EditPostResponseViewModel? = null
    var postId: String? = null
    var model: UpdatePostInputModel? = null
    var binding: ActivityEditTextPostBinding? = null
    var privacyStrings = arrayOf("Private", "Public")
    var privacyData = arrayOf("PRIVATE", "PUBLIC")
    var privacy = ""
    var AUDIO_RECORD_CODE = 121
    var mMediaSelectedList: MutableList<MediaItem> = mutableListOf<MediaItem>()
    private var prefsHelper: SharedPrefsHelper? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_text_post
    }

    override fun initViews() {
        postToolbarTitle.setText(getString(R.string.write_thoughts))


        binding = viewDataBinding as ActivityEditTextPostBinding

        editPostViewModel = ViewModelProviders.of(this).get(EditPostResponseViewModel::class.java)
        getIntentData()
        editPostViewModel?.init(this, model!!, postId!!)

        parentLayout = binding!!.clParent

        binding!!.viewModel = editPostViewModel
        binding!!.include.ivCancel.setOnClickListener(this)
        binding!!.ivEditVideo.setOnClickListener(this)
        //ivCancel!!.setOnClickListener(this)

        prefsHelper = SharedPrefsHelper(this)
        val name = prefsHelper!!.get(PreferenceKeys.PREF_USER_NAME, "")
        val profile = prefsHelper!!.get(PreferenceKeys.PREF_USER_IMAGE, "dasda")

        binding!!.tvName.text = name
        Picasso.with(this).load(profile).fit().centerCrop()
            .placeholder(R.drawable.ic_user_profle)
            .error(R.drawable.ic_user_profle)
            .into(binding!!.ivProfile)

        editPostViewModel?.getData()?.observe(this, object : Observer<AddPostResponse.Data> {
            override fun onChanged(response: AddPostResponse.Data?) {

                if (response != null) {
                    openMessageDialog(getString(R.string.post_updated_success))
                    binding!!.avPlayer.destroy()
                } else {

                    showToast(R.string.something_went_wrong)
                }

            }
        })

        editPostViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })

        setData()
        setSpinner()

        binding!!.switchView.trackDrawable = resources.getDrawable(R.drawable.round_switch_background_grey)
        binding!!.switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding!!.switchView.trackDrawable = resources.getDrawable(R.drawable.round_switch_background_grey)
            } else {
                binding!!.switchView.trackDrawable = resources.getDrawable(R.drawable.round_switch_background)
            }
        }
    }

    private fun setSpinner() {
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_item, privacyStrings)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding!!.spinPrivacy.adapter = arrayAdapter

        binding!!.spinPrivacy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                privacy = privacyData[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val position = privacyData.indexOf(model!!.privacy_option)
        binding!!.spinPrivacy.setSelection(position)

    }

    fun getIntentData() {
        val extraIntent = intent

        if (extraIntent != null) {
            postId = extraIntent.getStringExtra(AppConstants.POSTID)
            model = extraIntent.getSerializableExtra(AppConstants.POSTDATA) as UpdatePostInputModel
            privacy = model!!.privacy_option!!
        }
        when {
            model!!.post_upload_type.equals(AppConstants.VIDEO) -> {
                editPostViewModel!!.isVideoPost = true
                editPostViewModel!!.videoFilePath = model!!.post_upload_file.toString()
                setUpVideoView(model!!.post_upload_file.toString())
                postToolbarTitle.setText(getString(R.string.speak_thoughts))
            }
            model!!.post_upload_type.equals(AppConstants.AUDIO) -> {
                editPostViewModel!!.isVideoPost = false
                editPostViewModel!!.videoFilePath = model!!.post_upload_file.toString()
                setUpAudioView(model!!.post_upload_file.toString())
                postToolbarTitle.setText(getString(R.string.speak_thoughts))

            }
            else -> {
                editPostViewModel!!.isVideoPost = false
                setUpTextView()
                postToolbarTitle.setText(getString(R.string.write_thoughts))
            }
        }

    }

    fun setData() {
        binding!!.tvDesc.setText(model!!.description)
        binding!!.switchView.isChecked = model!!.is_anonymous.equals(AppConstants.YES)
    }

    private fun setUpAudioView(path: String) {
        binding!!.avPlayer.visibility = View.VISIBLE
        tv_heading.visibility = View.GONE
        videoView.visibility = View.GONE
        iv_edit_video.visibility = View.VISIBLE
        binding!!.avPlayer.withUrl(path)
    }

    private fun setUpTextView() {
        binding!!.avPlayer.visibility = View.GONE
        tv_heading.visibility = View.VISIBLE
        videoView.visibility = View.GONE
        iv_edit_video.visibility = View.GONE
    }


    private fun setUpVideoView(path: String) {
        tv_heading.visibility = View.GONE
        binding!!.avPlayer.visibility = View.GONE
        videoView.visibility = View.VISIBLE
        iv_edit_video.visibility = View.VISIBLE

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoPath(path)
        videoView.requestFocus()
        videoView.start()
    }

    override fun onBackPressed() {
        openMessageDialog(getString(R.string.discard_post))
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.iv_cancel -> {
                openMessageDialog(getString(R.string.discard_post))
            }

            R.id.iv_edit_video -> {
                when {
                    model!!.post_upload_type.equals(AppConstants.VIDEO) -> {
                        val builder = MediaOptions.Builder()
                        val options = builder.selectVideo().canSelectMultiVideo(false).build()
                        MediaPickerActivity.open(this@EditTextPostActivity, CommunityActivity.REQUEST_MEDIA, options)
                    }
                    model!!.post_upload_type.equals(AppConstants.AUDIO) -> {
                        val intent = Intent(this@EditTextPostActivity, RecordingActivity::class.java)
                        startActivityForResult(intent, AUDIO_RECORD_CODE)
                    }
                    model!!.post_upload_type.equals(AppConstants.TEXT) -> {


                    }
                }
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommunityActivity.REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                mMediaSelectedList = MediaPickerActivity
                    .getMediaItemSelected(data)
                startTrimActivity(
                    mMediaSelectedList[0].getPathOrigin(
                        this@EditTextPostActivity,
                        mMediaSelectedList[0].uriOrigin
                    )
                )

            }
        } else if (requestCode == AppConstants.REQUEST_TRIMMED_PATH_CODE) {

            val path = data!!.getStringExtra(AppConstants.EXTRA_VIDEO_URI)
            setUpVideoView(path)
            model!!.post_upload_file = path

        } else if (requestCode == AUDIO_RECORD_CODE) {

            val path = data?.extras?.get("audioFilePath")
            model!!.post_upload_file = path.toString()
            binding!!.avPlayer.visibility = View.VISIBLE
            binding!!.avPlayer.withUrl(path.toString())

        }

    }

    private fun startTrimActivity(path: String) {
        val intent = Intent(this@EditTextPostActivity, EditPostVideoTrimmerActivity::class.java)
        intent.putExtra(AppConstants.EXTRA_PATH, path)
        startActivityForResult(intent, AppConstants.REQUEST_TRIMMED_PATH_CODE)

    }

    override fun onVideoPathReady() {
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

    }

    override fun dispose() {

    }

}